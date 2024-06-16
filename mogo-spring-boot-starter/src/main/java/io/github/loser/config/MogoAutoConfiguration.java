/**
 * MogoAutoConfiguration.java 代码解读
 * 这段代码是Java语言编写的，属于Spring Boot框架下的自动配置类，用于配置和初始化Mogo框架。Mogo是一个基于Spring Boot的MongoDB数据源动态切换框架。下面将详细解释这段代码的功能和作用：
 * <p>
 * 导入依赖：代码首先导入了一系列的类和接口，这些是实现自动配置所需的依赖。
 * <p>
 * 类定义：MogoAutoConfiguration 类通过使用@EnableConfigurationProperties注解，启用了MogoLogicProperties和MogoDataSourceProperties两个配置类。
 * 这意味着这个自动配置类将会处理这两个配置类的属性。
 * <p>
 * Bean定义：
 * <p>
 * mogoDSClassAspect、mogoTSClassAspect、mogoDSMethodAspect、mogoTSMethodAspect：这些方法定义了几个Bean，它们分别用于数据源（DS）和事务源（TS）的切面编程。
 * mogoConfiguration：这个方法定义了一个MogoConfiguration的Bean，它用于配置Mogo框架的核心功能。
 * 构造函数：构造函数接收多个参数，包括MongoConverter、ApplicationContext等，用于初始化Mogo框架。在构造函数中，它首先调用enableFun方法来开启Mogo的相关功能，然后调用logBaseInfo方法来输出启动日志，最后调用MogoInitializer.init方法来进行Mogo的初始化操作。
 * <p>
 * enableFun方法：这个方法用于启用Mogo的相关功能。它通过扫描带有@EnableMogo注解的Bean，读取注解中的属性值，并将这些值设置到MogoEnableCache中，以便Mogo框架使用。
 * <p>
 * logBaseInfo方法：这个方法用于输出Mogo框架的启动信息，包括版本号、文档链接、作者等信息。同时，它还会输出Mogo各项功能的开启状态。
 * <p>
 * 总的来说，这段代码是Mogo框架在Spring Boot应用中的自动配置类，负责初始化和配置Mogo框架，使其能够在Spring Boot应用中正常工作。
 */
package io.github.loser.config;

import io.github.loser.aspect.ds.MogoDSClassAspect;
import io.github.loser.aspect.ds.MogoDSMethodAspect;
import io.github.loser.aspect.logic.IgnoreLogicClassAspect;
import io.github.loser.aspect.logic.IgnoreLogicMethodAspect;
import io.github.loser.aspect.ts.MogoTSClassAspect;
import io.github.loser.aspect.ts.MogoTSMethodAspect;
import io.github.loser.properties.MogoDataSourceProperties;
import io.github.loser.properties.MogoLogicProperties;
import io.github.loserya.config.MogoConfiguration;
import io.github.loserya.core.sdk.mapper.BaseMapper;
import io.github.loserya.global.BaseMapperContext;
import io.github.loserya.global.cache.MapperCache;
import io.github.loserya.hardcode.constant.MogoConstant;
import io.github.loserya.utils.MogoSpringContextUtils;
import io.github.loserya.utils.StringUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.core.annotation.Order;
import org.springframework.data.mongodb.core.MongoTemplate;

import java.io.Serializable;

/**
 * 基础必要配置
 *
 * @author loser
 * @since 1.0.0
 */
@Order(-1)
@EnableConfigurationProperties({MogoLogicProperties.class, MogoDataSourceProperties.class})
public class MogoAutoConfiguration implements BeanFactoryPostProcessor {

    private ConfigurableListableBeanFactory beanFactory;

    @Bean
    public IgnoreLogicMethodAspect ignoreLogicMethodAspect() {
        return new IgnoreLogicMethodAspect();
    }

    @Bean
    public IgnoreLogicClassAspect ignoreLogicClassAspect() {
        return new IgnoreLogicClassAspect();
    }

    @Bean
    public MogoDSClassAspect mogoDSClassAspect() {
        return new MogoDSClassAspect();
    }

    @Bean
    public MogoTSClassAspect mogoTSClassAspect() {
        return new MogoTSClassAspect();
    }

    @Bean
    public MogoDSMethodAspect mogoDSMethodAspect() {
        return new MogoDSMethodAspect();
    }

    @Bean
    public MogoTSMethodAspect mogoTSMethodAspect() {
        return new MogoTSMethodAspect();
    }

    @Bean
    @Order(-1)
    public MogoSpringContextUtils mogoSpringContextUtils() {
        return new MogoSpringContextUtils();
    }

    @Bean
    @Order(-1)
    public MogoConfiguration mogoConfiguration(@Qualifier("mongoTemplate") MongoTemplate mongoTemplate) {
        MogoConfiguration.instance().template(MogoConstant.MASTER_DS, mongoTemplate);
        return MogoConfiguration.instance();
    }

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
        this.beanFactory = beanFactory;
        // 把生成的 mapper 交给 spring 管理
        applySpringManageMapper();
    }

    /**
     * 将带有@MapperProxy的集合实体对应的 baseMapper 注册到容器中
     */
    private void applySpringManageMapper() {

        for (Class<?> aClass : MapperCache.MAPPER) {
            String beanName = StringUtils.firstToLowerCase(aClass.getSimpleName() + MogoConstant.BASE_MAPPER);
            if (!beanFactory.containsBean(beanName)) {
                BaseMapper<Serializable, ?> mapper = BaseMapperContext.getMapper(aClass);
                beanFactory.registerSingleton(beanName, mapper);
            }
        }

    }

}
