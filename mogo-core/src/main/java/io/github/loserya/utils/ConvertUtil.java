/**
 * ConvertUtil.java 代码解读
 * 这段代码是Java语言编写的，属于一个名为ConvertUtil的工具类，主要用于转换方法引用为属性名。下面是对代码的详细解释：
 * <p>
 * 类定义和构造函数
 * <p>
 * public class ConvertUtil：定义了一个名为ConvertUtil的公共类。
 * private ConvertUtil()：私有构造函数，防止外部实例化这个工具类。
 * 常量定义
 * <p>
 * public static final String GET = "get";
 * public static final String IS = "is";
 * 这两个常量用于识别Java Bean的getter和is方法。
 * 静态变量
 * <p>
 * private static final Map<Class<?>, String> CLASS_FIELD_META_MAP = new ConcurrentHashMap<>();
 * 这个并发哈希图用于缓存方法引用对应的属性名称，以提高性能。
 * 方法 convertToFieldName
 * <p>
 * 这个方法是类的核心功能，用于将方法引用转换为属性名。
 * 它接受一个SFunction<T, ?>类型的参数，这是一个函数式接口。 方法内部首先尝试从缓存中获取属性名，如果没有则通过反射和序列化机制提取方法名，并将其转换为属性名。
 * 如果方法名不是以get或is开头，则抛出异常。
 * 方法 getSerializedLambda
 * <p>
 * 这个方法用于获取实现了序列化的lambda函数。
 * 它通过反射调用writeReplace方法来获取SerializedLambda实例。 - 这个过程涉及到处理NoSuchMethodException、InvocationTargetException和IllegalAccessException异常。
 * 异常处理
 * <p>
 * 在convertToFieldName和getSerializedLambda方法中，通过捕获异常并抛出RuntimeException来处理潜在的错误情况。
 * 代码注释
 * <p>
 * 代码中包含了详细的中文注释，解释了每个部分的功能和目的。
 * 总的来说，这个ConvertUtil类提供了一种机制，可以将Java中的方法引用（特别是getter方法）转换为相应的属性名，这在处理反射或者动态代理时非常有用。
 * 通过使用缓存和序列化lambda表达式，这个工具类能够高效地执行其功能。
 */
package io.github.loserya.utils;

import io.github.loserya.utils.func.SFunction;

import java.io.Serializable;
import java.lang.invoke.SerializedLambda;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;


/**
 * 转转换工具
 *
 * @author loser
 * @since 1.0.0
 */
public class ConvertUtil {

    private ConvertUtil() {
    }

    public static final String GET = "get";

    public static final String IS = "is";

    /**
     * 缓存方法应用对应的属性名称
     */
    private static final Map<Class<?>, String> CLASS_FIELD_META_MAP = new ConcurrentHashMap<>();

    /**
     * 转换方法引用为属性名
     */
    public static <T> String convertToFieldName(SFunction<T, ?> fn) {

        SerializedLambda lambda = getSerializedLambda(fn);
        String cacheData = CLASS_FIELD_META_MAP.get(fn.getClass());
        if (Objects.nonNull(cacheData)) {
            return cacheData;
        }
        String methodName = lambda.getImplMethodName();
        if (methodName.startsWith(GET)) {
            methodName = methodName.substring(3);
        } else if (methodName.startsWith(IS)) {
            methodName = methodName.substring(2);
        } else {
            throw new IllegalArgumentException("无效的getter方法：" + methodName);
        }
        try {
            String fieldMeta = StringUtils.firstToLowerCase(methodName);
            CLASS_FIELD_META_MAP.put(fn.getClass(), fieldMeta);
            return fieldMeta;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    /**
     * 获取一个实现了序列化的lambda函数
     *
     * @param fn 目标函数
     * @return 实现了序列化的lambda函数
     */
    public static SerializedLambda getSerializedLambda(Serializable fn) {

        SerializedLambda lambda;
        try {
            // 提取SerializedLambda并缓存
            Method method = fn.getClass().getDeclaredMethod("writeReplace");
            method.setAccessible(Boolean.TRUE);
            lambda = (SerializedLambda) method.invoke(fn);
        } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
        return lambda;

    }
}