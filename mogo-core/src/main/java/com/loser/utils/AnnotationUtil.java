package com.loser.utils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Set;

/**
 * 注解工具
 *
 * @author loser
 * @date 2024/5/10
 */
public class AnnotationUtil {

    private AnnotationUtil() {
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

}
