/**
 * BaseMapperContext.java 代码解读
 * 这段代码是Java语言编写的，属于一个名为BaseMapperContext的类。这个类的主要功能是提供一个上下文环境，用于管理和获取BaseMapper实例。
 * BaseMapper是一个泛型接口，通常用于映射数据库中的表到Java对象。下面是对代码的详细解释：
 * <p>
 * 静态成员变量：
 * <p>
 * private final static Map<Class<?>, BaseMapper> mapper = new ConcurrentHashMap<>();
 * 定义了一个私有的静态成员变量mapper，它是一个ConcurrentHashMap，BaseMapper实例。键是类的类型，值是对应的BaseMapper实例。
 * <p>
 * 方法定义：
 * <p>
 * public static <I extends Serializable, T> BaseMapper<I, T> getMapper(Class<I> idType, Class<T> clazz)
 * public static <I extends Serializable, T> BaseMapper<I, T> getMapper(Class<T> clazz)
 * 这两个方法是BaseMapperContext类的核心。它们用于获取特定类型的BaseMapper实例。第一个方法接受两个参数：idType和clazz，而第二个方法只接受一个参数clazz。这两个方法都返回一个BaseMapper实例。
 * <p>
 * 方法实现：
 * <p>
 * 在getMapper(Class<T> clazz)方法中，首先尝试从mapper映射中获取对应clazz的BaseMapper实例。
 * 如果未找到（即baseMapper为null），则进入同步块。在同步块内，再次尝试获取BaseMapper实例。
 * 如果仍然未找到，则通过MapperFactory.getMapper(clazz)创建一个新的BaseMapper实例，并将其放入mapper映射中。
 * 最后，返回找到或创建的BaseMapper实例。
 * 线程安全：
 * <p>
 * 由于使用了ConcurrentHashMap和同步块，这个类在多线程环境下是线程安全的。
 * 泛型使用：
 * <p>
 * 方法使用了泛型<I extends Serializable, T>，这意味着这些方法可以适用于任何实现了Serializable接口的类型I和任意类型T。
 * 总的来说，这个类提供了一个线程安全的方式来管理和获取BaseMapper实例，这些实例用于将Java对象映射到本地缓存 MAP 中。
 */
package io.github.loserya.global;

import io.github.loserya.config.MogoConfiguration;
import io.github.loserya.core.mapper.factory.MapperFactory;
import io.github.loserya.core.sdk.mapper.BaseMapper;

import java.io.Serializable;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

/**
 * baseMapper 上下文
 *
 * @author loser
 * @since 1.0.0
 */
@SuppressWarnings("all")
public class BaseMapperContext {

    private final static Map<Class<?>, BaseMapper> mapper = new ConcurrentHashMap<>();

    public static Map<Class<?>, BaseMapper> getMapper() {
        return mapper;
    }

    public static <I extends Serializable, T> BaseMapper<I, T> getMapper(Class<I> idType, Class<T> clazz) {
        return getMapper(clazz);
    }

    public static <I extends Serializable, T> BaseMapper<I, T> getMapper(Class<T> clazz) {

        BaseMapper baseMapper = mapper.get(clazz);
        if (Objects.isNull(baseMapper)) {
            synchronized (BaseMapperContext.class) {
                baseMapper = mapper.get(clazz);
                try {
                    if (Objects.nonNull(baseMapper)) {
                        return baseMapper;
                    }
                    BaseMapper handler = MapperFactory.getMapper(clazz);
                    mapper.put(clazz, handler);
                    return handler;
                } finally {
                    MogoConfiguration.instance().mapperLogic(clazz);
                }
            }
        }
        return baseMapper;

    }

}
