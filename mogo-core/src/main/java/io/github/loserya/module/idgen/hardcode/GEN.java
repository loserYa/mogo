package io.github.loserya.module.idgen.hardcode;


public enum GEN {

    /**
     * 生成mongoDB自带的_id
     **/
    OBJECT_ID,

    /**
     * 生成UUID
     *
     * @since 2023/2/13 16:09
     */
    UUID,

    /**
     * ULID是一种比UUID更好的方案，它具有可排序性、可读性、低碰撞率、短且轻量级、安全等优势。
     * 在分布式系统中，使用ULID可以提高数据库查询的效率，同时保证数据的唯一性。
     * 如果你正在构建一个分布式系统，不妨考虑使用ULID来标识你的数据和实体。
     **/
    ULID,

    /**
     * 生成雪花算法
     */
    SNOW,

    /**
     * 生成自增id 不推荐使用这个,会在mongodb 中生成一个 SysAutoIDCount 用于记录 id 且每次都需要多操作一次 mongodb 如果需要使用可以重写 使用 redis 等实现方案
     * sysAutoCount
     */
    AUTO,

    /**
     * 用户自定义 使用这个需要将实现类注册为 springBean
     */
    CUSTOM;


}
