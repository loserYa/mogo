package io.github.loserya.global.cache;


import io.github.loserya.module.logic.entity.LogicDeleteResult;

import java.util.HashMap;
import java.util.Map;

/**
 * 全局逻辑删除基础信息缓存
 *
 * @author loser
 * @since 1.0.0
 */
public class CollectionLogicDeleteCache {

    /**
     * 目标文档对应的逻辑删除字段
     */
    public static final Map<Class<?>, LogicDeleteResult> logicDeleteResultHashMap = new HashMap<>();

    public static LogicDeleteResult getRes(Class<?> clazz) {
        return logicDeleteResultHashMap.get(clazz);
    }

}
