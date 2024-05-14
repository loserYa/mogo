package io.github.loserya.global.cache;

/**
 * mogo 功能统一开关
 *
 * @author loser
 * @since 1.0.0
 */
public class MogoEnableCache {

    /**
     * 是否开启基础功能
     */
    public static boolean base = false;

    /**
     * 是否开启逻辑删除功能
     */
    public static boolean logic = false;

    /**
     * 是否开启逻辑字段填充功能
     */
    public static boolean autoFill = false;

    /**
     * 是否开启多数据源功能
     */
    public static boolean dynamicDs = false;

    /**
     * 禁止全表更新及全表删除
     */
    public static boolean banFullTable = false;

    /**
     * 是否开启事务功能
     */
    public static boolean transaction = false;

}
