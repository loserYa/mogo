package com.loser.function.replacer;


import com.loser.utils.func.BoolFunction;

import java.lang.reflect.Method;

/**
 * 替换器 只会执行首个命中的替换器
 *
 * @author loser
 * @date 2024/4/30
 */
public interface Replacer {

    default int order() {
        return 1;
    }

    Object invoke(Class<?> clazz, Object proxy, Object target, Method method, Object[] args) throws Throwable;

    BoolFunction supplier();

    default Object[] build(Object... args) {
        return args;
    }

}