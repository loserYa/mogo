package com.loser.core.cache;

import com.loser.core.sdk.mapper.BaseMapper;
import com.loser.utils.ExceptionUtils;

import java.io.Serializable;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

public class BaseContext {

    private final static Map<Class<?>, BaseMapper> mapper = new ConcurrentHashMap<>();

    public static <I extends Serializable, T> BaseMapper<I, T> getMapper(Class<I> idType, Class<T> clazz) {
        BaseMapper baseMapper = mapper.get(clazz);
        if (Objects.isNull(baseMapper)) {
            ExceptionUtils.mpe("mapper un exist");
        }
        return baseMapper;
    }

    public static <I extends Serializable, T> void register(Class<T> tClass, BaseMapper<I, T> baseMapper) {
        mapper.put(tClass, baseMapper);
    }

}
