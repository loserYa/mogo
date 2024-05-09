package com.loser.core.cache;

import com.loser.core.proxy.factory.MapperFactory;
import com.loser.core.sdk.mapper.BaseMapper;

import java.io.Serializable;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

public class BaseContext {

    private final static Map<Class<?>, BaseMapper> mapper = new ConcurrentHashMap<>();

    public static <I extends Serializable, T> BaseMapper<I, T> getMapper(Class<I> idType, Class<T> clazz) {
        return getMapper(clazz);
    }

    public static <I extends Serializable, T> BaseMapper<I, T> getMapper(Class<T> clazz) {

        BaseMapper baseMapper = mapper.get(clazz);
        if (Objects.isNull(baseMapper)) {
            synchronized (BaseContext.class) {
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
