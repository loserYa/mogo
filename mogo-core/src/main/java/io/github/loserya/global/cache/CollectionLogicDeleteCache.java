package io.github.loserya.global.cache;


import io.github.loserya.module.logic.entity.LogicDeleteResult;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * 全局逻辑删除基础信息缓存
 *
 * @author loser
 * @since 1.0.0
 */
public class CollectionLogicDeleteCache {

    /**
     * 是否关闭逻辑删除
     */
    public static boolean isClose() {
        return !MogoEnableCache.logic || getIgnoreLogic();
    }

    /**
     * 是否开启逻辑删除
     */
    public static boolean IsLogic() {
        return MogoEnableCache.logic && !getIgnoreLogic();
    }

    /**
     * 标记方法需要进行逻辑删除忽略
     */
    private static final ThreadLocal<Boolean> ignoreLogic = new InheritableThreadLocal<>();

    public static void setIgnoreLogic(Boolean ignore) {
        ignoreLogic.set(ignore);
    }

    public static boolean getIgnoreLogic() {

        Boolean ignore = ignoreLogic.get();
        return Objects.nonNull(ignore) && ignore;

    }

    public static void clear() {
        ignoreLogic.remove();
    }

    /**
     * 目标文档对应的逻辑删除字段
     */
    public static final Map<Class<?>, LogicDeleteResult> logicDeleteResultHashMap = new HashMap<>();

    public static LogicDeleteResult getRes(Class<?> clazz) {
        return logicDeleteResultHashMap.get(clazz);
    }

}
