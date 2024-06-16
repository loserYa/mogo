package io.github.loserya.core.handler;


import io.github.loserya.core.anno.MapperProxy;
import io.github.loserya.core.anno.MapperProxyScanner;
import io.github.loserya.core.sdk.impl.MogoServiceImpl;
import io.github.loserya.global.cache.MapperCache;
import io.github.loserya.utils.CollectionUtils;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.core.type.ClassMetadata;
import org.springframework.core.type.filter.AnnotationTypeFilter;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class MapperProxyHandler implements ImportBeanDefinitionRegistrar {

    @Override
    public void registerBeanDefinitions(AnnotationMetadata metadata, BeanDefinitionRegistry registry) {

        AnnotationAttributes scanner = AnnotationAttributes.fromMap(metadata.getAnnotationAttributes(MapperProxyScanner.class.getName()));
        if (Objects.isNull(scanner)) {
            return;
        }
        String[] values = scanner.getStringArray("value");
        for (String basePackage : values) {
            Set<Class<?>> classSet = findAnnotatedClasses(basePackage);
            if (CollectionUtils.isNotEmpty(classSet)) {
                MapperCache.MAPPER.addAll(classSet);
            }
            Set<Class<?>> byService = findByService(basePackage);
            if (CollectionUtils.isNotEmpty(byService)) {
                MapperCache.MAPPER.addAll(byService);
            }
        }

    }

    /**
     * 扫描指定包中带有指定注解的类。
     *
     * @param basePackage 要扫描的包名
     * @return 带有指定注解的类的集合
     */
    private static Set<Class<?>> findByService(String basePackage) {

        Set<Class<?>> classes = new HashSet<>();
        ClassPathScanningCandidateComponentProvider scanner = new ClassPathScanningCandidateComponentProvider(false);
        scanner.addIncludeFilter((metadataReader, metadataReaderFactory) -> {
            ClassMetadata classMetadata = metadataReader.getClassMetadata();
            String className = classMetadata.getClassName();
            Class<?> aClass;
            try {
                aClass = Class.forName(className);
            } catch (ClassNotFoundException e) {
                return false;
            }
            return MogoServiceImpl.class.isAssignableFrom(aClass);

        });
        Set<BeanDefinition> beanDefinitions = scanner.findCandidateComponents(basePackage);
        for (BeanDefinition beanDefinition : beanDefinitions) {
            try {
                Class<?> clazz = Class.forName(beanDefinition.getBeanClassName());
                MogoServiceImpl service = (MogoServiceImpl) clazz.newInstance();
                classes.add(service.getTargetClass());
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        return classes;
    }


    /**
     * 扫描指定包中带有指定注解的类。
     *
     * @param basePackage 要扫描的包名
     * @return 带有指定注解的类的集合
     */
    private static Set<Class<?>> findAnnotatedClasses(String basePackage) {

        Set<Class<?>> classes = new HashSet<>();
        // 创建扫描器并设置为只扫描带有自定义注解的类
        ClassPathScanningCandidateComponentProvider scanner = new ClassPathScanningCandidateComponentProvider(false);
        scanner.addIncludeFilter(new AnnotationTypeFilter(MapperProxy.class));
        // 扫描指定包中的所有类
        Set<BeanDefinition> beanDefinitions = scanner.findCandidateComponents(basePackage);
        // 加载并收集带有自定义注解的类
        for (BeanDefinition beanDefinition : beanDefinitions) {
            try {
                Class<?> clazz = Class.forName(beanDefinition.getBeanClassName());
                classes.add(clazz);
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        }
        return classes;
    }

}
