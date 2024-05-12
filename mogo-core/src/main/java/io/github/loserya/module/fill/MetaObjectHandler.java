package io.github.loserya.module.fill;

import io.github.loserya.module.fill.entity.FiledMeta;

/**
 * 全局字段填充处理器
 *
 * @author loser
 * @since 2024/5/12
 */
public interface MetaObjectHandler {

    default Integer order() {
        return 1;
    }

    FiledMeta insertFill(Class<?> clazz, Object obj);

    FiledMeta updateFill(Class<?> clazz, Object obj);

}
