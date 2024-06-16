package io.github.loserya.utils;

import org.springframework.data.annotation.Id;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 字节码工具
 *
 * @author loser
 * @since 1.0.0
 */
public class ClassUtil {

    private ClassUtil() {
    }

    /**
     * 缓存目标类上的泛型值
     */
    private static final Map<Class<?>, Class<?>> MAPPER_CACHE = new HashMap<>(64);

    /**
     * 缓存mongo实体的主键字段
     */
    private static final Map<Class<?>, Field> FIELD_CACHE = new HashMap<>(64);

    private static final Map<String, Field> FIELD_MAP = new HashMap<>();

    public static Field getField(Class<?> clazz, String name) {

        if (clazz.equals(Object.class)) {
            throw ExceptionUtils.mpe(String.format("NoSuchFieldException %s", name));
        }
        try {
            return clazz.getDeclaredField(name);
        } catch (Exception ignore) {
            return getFieldWitchCache(clazz.getSuperclass(), name);
        }

    }

    public static Field getFieldWitchCache(Class<?> clazz, String name) {

        String key = String.format("%s.%s", clazz.getName(), name);
        Field field = FIELD_MAP.get(key);
        if (Objects.nonNull(field)) {
            return field;
        }
        field = getField(clazz, name);
        field.setAccessible(true);
        FIELD_MAP.put(key, field);
        return field;

    }

    /**
     * 获取mongo集合实体的主键值
     *
     * @param obj 目标对象
     * @return id对应的值
     */
    public static Serializable getId(Object obj) {

        Field field = getIdField(obj);
        field.setAccessible(true);
        try {
            return (Serializable) field.get(obj);
        } catch (IllegalAccessException e) {
            throw ExceptionUtils.mpe("not exist id value");
        }

    }

    /**
     * 获取mongo集合实体的主键字段
     *
     * @param obj 目标对象
     * @return 主建字段
     */
    public static Field getIdField(Object obj) {

        Class<?> clazz = obj.getClass();
        Field result = FIELD_CACHE.get(clazz);
        if (Objects.nonNull(result)) {
            return result;
        }
        for (Field field : clazz.getDeclaredFields()) {
            Id annotation = field.getAnnotation(Id.class);
            if (Objects.nonNull(annotation)) {
                field.setAccessible(true);
                FIELD_CACHE.put(clazz, field);
                return field;
            }
        }
        throw ExceptionUtils.mpe("no exist id");

    }

    /**
     * 获取对象上的泛型
     *
     * @param obj 目标类
     * @return 类名
     */
    public static Class<?> getTClass(Object obj) {

        if (Objects.isNull(obj)) {
            return null;
        }
        Class<?> clazz = obj.getClass();
        Class<?> result = MAPPER_CACHE.get(clazz);
        if (Objects.nonNull(result)) {
            return result;
        }
        result = getGenericClass(obj.getClass(), 1);
        MAPPER_CACHE.put(clazz, result);
        return result;

    }

    public static Class<?> getGenericClass(Class<?> clazz, int index) {
        Type genericSuperclass = clazz.getGenericSuperclass();
        if (!(genericSuperclass instanceof ParameterizedType)) {
            return Object.class; // 若未指定泛型类型，则返回 Object.class
        }
        ParameterizedType parameterizedType = (ParameterizedType) genericSuperclass;
        Type[] actualTypeArguments = parameterizedType.getActualTypeArguments();
        if (index >= actualTypeArguments.length || index < 0) {
            throw new IllegalArgumentException("Index out of bounds");
        }
        if (actualTypeArguments[index] instanceof Class) {
            return (Class<?>) actualTypeArguments[index];
        } else {
            return Object.class; // 若泛型参数不是 Class 类型，则返回 Object.class
        }
    }

    private static final Set<String> OBJ_METHOD;

    static {
        OBJ_METHOD = Arrays.stream(Object.class.getMethods()).map(Method::getName).collect(Collectors.toSet());
    }

    /**
     * 判断指定的方法是否属于Object类的方法。
     *
     * @param method 要检查的方法实例
     * @return 如果该方法是Object类的方法，则返回true；否则返回false。
     */
    public static boolean isObjectMethod(Method method) {
        return OBJ_METHOD.contains(method.getName());
    }

}
