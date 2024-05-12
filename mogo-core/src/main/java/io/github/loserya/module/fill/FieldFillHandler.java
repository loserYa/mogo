package io.github.loserya.module.fill;

/**
 * 注解字段填充处理器
 *
 * @author loser
 * @since 2024/5/12
 */
public interface FieldFillHandler<T> {

    T invoke();

}
