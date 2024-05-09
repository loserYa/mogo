package com.loser.function.replacer;


import com.loser.utils.func.BoolFunction;

import java.lang.reflect.Method;

/**
 * 替换器接
 *
 * @author loser
 * @date 2024/4/30
 */
public interface Replacer {

    default int order() {
        return Integer.MIN_VALUE;
    }

    Object invoke(Class<?> clazz, Object proxy, Object target, Method method, Object[] args) throws Throwable;

    BoolFunction supplier();

    default Object[] build(Object... args) {
        return args;
    }

}