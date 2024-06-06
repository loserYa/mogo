/**
 * 这段代码是Java语言编写的，属于一个名为mogo的项目的一部分，具体位于mogo-core模块的MapperFactory.java文件中。
 * 该文件的主要功能是提供一个工厂类，用于创建和配置BaseMapper实例。下面是对代码的详细解释：
 * <p>
 * getMapper方法:
 * <p>
 * public static BaseMapper getMapper(Class<?> clazz) 是一个静态方法，用于根据传入的类clazz创建一个BaseMapper实例。
 * 如果MogoEnableCache.base为false，则直接返回一个DefaultBaseMapper实例。
 * 如果为true，则创建一个代理实例，并记录相关日志。
 * mapper方法:
 * <p>
 * private static void mapper(Class<?> clazz) 是一个私有静态方法，用于映射实体与逻辑删除字段的关系。
 * 首先检查MogoEnableCache.logic是否启用。
 * 使用AnnotationHandler.getAnnotationOnFiled方法获取类clazz上的CollectionLogic注解。
 * 根据注解或全局配置（MogoConfiguration）设置逻辑删除字段和值。
 * 逻辑删除处理:
 * <p>
 * 代码中处理了逻辑删除的逻辑，即不是真正从数据库中删除记录，而是通过标记一个字段来表示记录已被删除。
 * 这通过检查注解CollectionLogic和全局配置来实现。
 * 动态代理: 使用Proxy.newProxyInstance创建了一个动态代理，这允许在调用BaseMapper的方法时添加额外的处理逻辑，例如缓存处理。
 * <p>
 * 日志记录:
 * <p>
 * 在创建代理实例后，使用LOGGER.info记录了一条日志信息。
 * 总的来说，这段代码是mogo项目中用于创建和配置数据映射器（BaseMapper）的工厂类，支持逻辑删除和缓存功能。通过使用注解和配置，它允许灵活地定义如何处理数据的映射和逻辑删除。
 */
package io.github.loserya.core.mapper.factory;

import io.github.loserya.config.MogoConfiguration;
import io.github.loserya.core.mapper.MapperProxy;
import io.github.loserya.core.sdk.mapper.BaseMapper;
import io.github.loserya.core.sdk.mapper.DefaultBaseMapper;
import io.github.loserya.global.cache.CollectionLogicDeleteCache;
import io.github.loserya.global.cache.MogoEnableCache;
import io.github.loserya.hardcode.constant.MogoConstant;
import io.github.loserya.module.logic.AnnotationHandler;
import io.github.loserya.module.logic.CollectionLogic;
import io.github.loserya.module.logic.entity.ClassAnnotationFiled;
import io.github.loserya.module.logic.entity.LogicDeleteResult;
import io.github.loserya.module.logic.entity.LogicProperty;
import io.github.loserya.utils.StringUtils;
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
 * @since 1.0.0
 */
public class MapperFactory {

    private static final Log LOGGER = LogFactory.getLog(MapperFactory.class);

    @SuppressWarnings("all")
    public static BaseMapper getMapper(Class<?> clazz) {

        BaseMapper mapper = new DefaultBaseMapper<>(clazz);
        if (!MogoEnableCache.base) {
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

        if (!MogoEnableCache.logic) {
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
