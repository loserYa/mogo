package com.loser.core.cache;

import com.loser.core.proxy.factory.MapperFactory;
import com.loser.core.sdk.mapper.BaseMapper;
import com.loser.utils.SpringContextUtil;
import org.springframework.data.mongodb.core.MongoTemplate;

import java.io.Serializable;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

@SuppressWarnings("all")
public class BaseContext {

    private final static Map<Class<?>, BaseMapper> mapper = new ConcurrentHashMap<>();

    public static <I extends Serializable, T> BaseMapper<I, T> getMapper(Class<I> idType, Class<T> clazz) {
        return getMapper(clazz, null);
    }

    public static <I extends Serializable, T> BaseMapper<I, T> getMapper(Class<T> clazz, MongoTemplate mongoTemplate) {

        BaseMapper baseMapper = mapper.get(clazz);
        if (Objects.isNull(baseMapper)) {
            synchronized (BaseContext.class) {
                baseMapper = mapper.get(clazz);
                if (Objects.nonNull(baseMapper)) {
                    return baseMapper;
                }
                BaseMapper handler;
                if (Objects.isNull(mongoTemplate)) {
                    handler = MapperFactory.getMapper(SpringContextUtil.getBean(MongoTemplate.class), clazz);
                } else {
                    handler = MapperFactory.getMapper(mongoTemplate, clazz);
                }
                mapper.put(clazz, handler);
                return handler;
            }
        }
        return baseMapper;

    }


}
