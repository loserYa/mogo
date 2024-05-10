package com.loser.core.mapper.factory;

import com.loser.config.MogoConfiguration;
import com.loser.core.mapper.MapperProxy;
import com.loser.core.sdk.mapper.BaseMapper;
import com.loser.core.sdk.mapper.DefaultBaseMapper;
import com.loser.global.cache.CollectionLogicDeleteCache;
import com.loser.global.cache.MogoCache;
import com.loser.hardcode.constant.MogoConstant;
import com.loser.module.logic.AnnotationHandler;
import com.loser.module.logic.CollectionLogic;
import com.loser.module.logic.entity.ClassAnnotationFiled;
import com.loser.module.logic.entity.LogicDeleteResult;
import com.loser.module.logic.entity.LogicProperty;
import com.loser.utils.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.lang.reflect.Field;
import java.lang.reflect.Proxy;
import java.util.Map;
import java.util.Objects;

/**
 * BaseMapper 工厂
 *
 * @author loser
 * @date 2024/5/9
 */
public class MapperFactory {

    private static final Log LOGGER = LogFactory.getLog(MapperFactory.class);

    @SuppressWarnings("all")
    public static BaseMapper getMapper(Class<?> clazz) {

        BaseMapper mapper = new DefaultBaseMapper<>(clazz);
        if (!MogoCache.open) {
            return mapper;
        }
        Class<? extends BaseMapper> mapperClass = mapper.getClass();
        mapper(clazz);
        Object o = Proxy.newProxyInstance(mapperClass.getClassLoader(), mapperClass.getInterfaces(), new MapperProxy(mapper));
        LOGGER.info(MogoConstant.LOG_PRE + String.format("init mogo Mapper proxy finish %s %s", clazz.getName(), o));
        return (BaseMapper) o;

    }

    /**
     * 映射实体与逻辑删除字段的关系
     */
    private static void mapper(Class<?> clazz) {

        if (!CollectionLogicDeleteCache.open) {
            return;
        }
        Map<Class<?>, LogicDeleteResult> logicDeleteResultHashMap = CollectionLogicDeleteCache.logicDeleteResultHashMap;
        if (logicDeleteResultHashMap.containsKey(clazz)) {
            return;
        }
        ClassAnnotationFiled<CollectionLogic> targetInfo = AnnotationHandler.getAnnotationOnFiled(clazz, CollectionLogic.class);
        LogicProperty logicProperty = MogoConfiguration.instance().getLogicProperty();
        // 优先使用每个对象自定义规则
        if (Objects.nonNull(targetInfo)) {
            CollectionLogic annotation = targetInfo.getTargetAnnotation();
            if (annotation.close()) {
                logicDeleteResultHashMap.put(clazz, null);
                return;
            }
            LogicDeleteResult result = new LogicDeleteResult();
            Field field = targetInfo.getField();
            org.springframework.data.mongodb.core.mapping.Field collectionField = field.getAnnotation(org.springframework.data.mongodb.core.mapping.Field.class);
            String column = Objects.nonNull(collectionField) && StringUtils.isNotBlank(collectionField.value()) ? collectionField.value() : field.getName();
            result.setFiled(field.getName());
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
            result.setFiled(logicProperty.getLogicDeleteField());
            result.setLogicDeleteValue(logicProperty.getLogicDeleteValue());
            result.setLogicNotDeleteValue(logicProperty.getLogicNotDeleteValue());
            logicDeleteResultHashMap.put(clazz, result);
            return;
        }
        logicDeleteResultHashMap.put(clazz, null);

    }

}
