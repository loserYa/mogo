package io.github.loserya.module.fill;

import io.github.loserya.module.fill.entity.FiledMeta;

import java.util.Collections;
import java.util.List;

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

    default FiledMeta insertFill(Class<?> clazz, Object obj) {
        return null;
    }

    default FiledMeta updateFill(Class<?> clazz, Object obj) {
        return null;
    }

    default List<FiledMeta> insertFills(Class<?> clazz, Object obj) {
        return Collections.singletonList(insertFill(clazz, obj));
    }

    default List<FiledMeta> updateFills(Class<?> clazz, Object obj) {
        return Collections.singletonList(updateFill(clazz, obj));
    }

}
