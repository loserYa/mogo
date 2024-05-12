package io.github.loserya.module.fill.hardcode;

public enum FieldFill {

    /**
     * 默认不处理
     */
    DEFAULT,

    /**
     * 插入时填充字段
     */
    INSERT,

    /**
     * 更新时填充字段
     */
    UPDATE,

    /**
     * 插入和更新时都进行填充字段
     */
    INSERT_UPDATE
}
