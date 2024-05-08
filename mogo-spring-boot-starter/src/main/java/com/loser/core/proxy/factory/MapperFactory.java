package com.loser.core.proxy.factory;

import com.loser.core.anno.CollectionLogic;
import com.loser.core.cache.global.CollectionLogicDeleteCache;
import com.loser.core.logic.AnnotationHandler;
import com.loser.core.logic.entity.ClassAnnotationFiled;
import com.loser.core.logic.entity.LogicDeleteResult;
import com.loser.core.logic.entity.LogicProperty;
import com.loser.core.proxy.MapperProxy;
import com.loser.core.sdk.mapper.BaseMapper;
import com.loser.core.sdk.mapper.DefaultBaseMapper;
import com.loser.utils.StringUtils;
import org.springframework.data.mongodb.core.MongoTemplate;

import java.lang.reflect.Field;
import java.lang.reflect.Proxy;
import java.util.Map;
import java.util.Objects;

public class MapperFactory {

    @SuppressWarnings("all")
    public static BaseMapper getMapper(MongoTemplate template, Class<?> clazz) {

        BaseMapper mapper = new DefaultBaseMapper<>(template, clazz);
        Class<? extends BaseMapper> mapperClass = mapper.getClass();
        mapper(clazz);
        return (BaseMapper) Proxy.newProxyInstance(mapperClass.getClassLoader(), mapperClass.getInterfaces(), new MapperProxy(mapper));

    }

    private static void mapper(Class<?> clazz) {

        Map<Class<?>, LogicDeleteResult> logicDeleteResultHashMap = CollectionLogicDeleteCache.logicDeleteResultHashMap;
        if (logicDeleteResultHashMap.containsKey(clazz)) {
            return;
        }
        ClassAnnotationFiled<CollectionLogic> targetInfo = AnnotationHandler.getAnnotationOnFiled(clazz, CollectionLogic.class);
        // todo loser
        LogicProperty logicProperty = new LogicProperty();
        // 优先使用每个对象自定义规则
        if (Objects.nonNull(targetInfo)) {
            CollectionLogic annotation = targetInfo.getTargetAnnotation();
            if (annotation.close()) {
                logicDeleteResultHashMap.put(clazz, null);
                return;
            }
            LogicDeleteResult result = new LogicDeleteResult();
            Field field = targetInfo.getField();
            String column = field.getName();
            result.setColumn(column);
            result.setLogicDeleteValue(StringUtils.isNotBlank(annotation.delval()) ? annotation.delval() : logicProperty.getLogicDeleteValue());
            result.setLogicNotDeleteValue(StringUtils.isNotBlank(annotation.value()) ? annotation.value() : logicProperty.getLogicNotDeleteValue());
            logicDeleteResultHashMap.put(clazz, result);
            return;
        }

        // 其次使用全局配置规则
        if (StringUtils.isNotBlank(logicProperty.getLogicDeleteField())
                && StringUtils.isNotBlank(logicProperty.getLogicDeleteValue())
                && StringUtils.isNotBlank(logicProperty.getLogicNotDeleteValue())) {
            LogicDeleteResult result = new LogicDeleteResult();
            result.setColumn(logicProperty.getLogicDeleteField());
            result.setLogicDeleteValue(logicProperty.getLogicDeleteValue());
            result.setLogicNotDeleteValue(logicProperty.getLogicNotDeleteValue());
            logicDeleteResultHashMap.put(clazz, result);
            return;
        }
        logicDeleteResultHashMap.put(clazz, null);

    }

}
