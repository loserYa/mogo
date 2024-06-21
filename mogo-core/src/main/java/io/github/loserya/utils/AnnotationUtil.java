package io.github.loserya.utils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

/**
 * 注解工具
 *
 * @author loser
 * @since 1.0.0
 */
public class AnnotationUtil {

    private AnnotationUtil() {
    }

    /**
     * 获取指定类或其代理类上的指定注解
     *
     * @param clazz           要检查的类
     * @param annotationClass 要获取的注解类型
     * @param <T>             注解类型
     * @return 如果找到了指定的注解，则返回该注解，否则返回 null
     */
    public static <T extends Annotation> T getAnnotation(Class<?> clazz, Class<T> annotationClass) {

        // 检查传入的类是否是代理类，如果是则获取其代理的接口
        Class<?> targetClass = clazz;
        if (isCglibProxy(clazz)) {
            targetClass = clazz.getSuperclass();
        } else if (Proxy.isProxyClass(clazz)) {
            targetClass = clazz.getInterfaces()[0];
        }

        // 检查类上是否有指定的注解
        return targetClass.getAnnotation(annotationClass);

    }

    /**
     * 判断是否是 CGLIB 代理类
     *
     * @param clazz 要检查的类
     * @return 如果是 CGLIB 代理类，则返回 true，否则返回 false
     */
    private static boolean isCglibProxy(Class<?> clazz) {
        return clazz.getName().contains("EnhancerBy");
    }

    public static <T extends Annotation> T findFirstAnnotation(Class<T> annotationClazz, Field field) {
        return getAnnotation(annotationClazz, new HashSet<>(), field.getDeclaredAnnotations());
    }

    @SuppressWarnings("unchecked")
    private static <T extends Annotation> T getAnnotation(Class<T> annotationClazz, Set<Class<? extends Annotation>> annotationSet, Annotation... annotations) {
        for (Annotation annotation : annotations) {
            if (annotationSet.add(annotation.annotationType())) {
                if (annotationClazz.isAssignableFrom(annotation.annotationType())) {
                    return (T) annotation;
                }
                annotation = getAnnotation(annotationClazz, annotationSet, annotation.annotationType().getDeclaredAnnotations());
                if (annotation != null) {
                    return (T) annotation;
                }
            }
        }
        return null;
    }

    /**
     * 查找类或者公共方法上是否存在改注解
     */
    public static boolean isExistMethodAndFunction(Class<?> aClass, Class<? extends Annotation> anno) {

        if (aClass.isAnnotationPresent(anno)) {
            return true;
        }
        for (Method method : aClass.getMethods()) {
            if (method.isAnnotationPresent(anno)) {
                return true;
            }
        }
        return false;

    }

    public static <T extends Annotation> Map<Method, T> buildByClass(Class<?> aClass, Class<T> anno) {

        Map<Method, T> result = new HashMap<>();
        T baseAnno = aClass.getAnnotation(anno);
        for (Method method : aClass.getDeclaredMethods()) {
            T annotation = method.getAnnotation(anno);
            result.put(method, Objects.isNull(annotation) ? baseAnno : annotation);
        }
        return result;

    }

}
