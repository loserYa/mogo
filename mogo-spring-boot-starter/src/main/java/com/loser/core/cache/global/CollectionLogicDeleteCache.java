package com.loser.core.cache.global;


import com.loser.core.logic.entity.LogicDeleteResult;
import com.loser.core.logic.entity.LogicProperty;

import java.util.HashMap;
import java.util.Map;

/**
 * 全局逻辑删除基础信息缓存
 *
 * @author loser
 * @date 2024/4/29
 */
public class CollectionLogicDeleteCache {

    /**
     * 是否开启逻辑删除功能
     */
    public static Boolean open = true;

    /**
     * 逻辑删除配置
     */
    public static LogicProperty logicProperty = new LogicProperty();

    /**
     * 目标文档对应的逻辑删除字段
     */
    public static final Map<Class<?>, LogicDeleteResult> logicDeleteResultHashMap = new HashMap<>();

    public static LogicDeleteResult getRes(Class<?> clazz) {
        return logicDeleteResultHashMap.get(clazz);
    }

}
