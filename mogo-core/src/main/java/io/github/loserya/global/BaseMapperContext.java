package io.github.loserya.global;

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
 * @date 2024/5/9
 */
@SuppressWarnings("all")
public class BaseMapperContext {

    private final static Map<Class<?>, BaseMapper> mapper = new ConcurrentHashMap<>();

    public static <I extends Serializable, T> BaseMapper<I, T> getMapper(Class<I> idType, Class<T> clazz) {
        return getMapper(clazz);
    }

    public static <I extends Serializable, T> BaseMapper<I, T> getMapper(Class<T> clazz) {

        BaseMapper baseMapper = mapper.get(clazz);
        if (Objects.isNull(baseMapper)) {
            synchronized (BaseMapperContext.class) {
                baseMapper = mapper.get(clazz);
                if (Objects.nonNull(baseMapper)) {
                    return baseMapper;
                }
                BaseMapper handler = MapperFactory.getMapper(clazz);
                mapper.put(clazz, handler);
                return handler;
            }
        }
        return baseMapper;

    }

}
