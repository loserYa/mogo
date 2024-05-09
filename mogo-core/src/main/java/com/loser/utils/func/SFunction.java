package com.loser.utils.func;

import java.io.Serializable;

/**
 * 支持序列化的 Function
 * 为了获取字段名字
 *
 * @author lsoer
 * @since 2018-05-12
 */
@FunctionalInterface
public interface SFunction<T, R> extends Serializable {

    R apply(T t);

}
