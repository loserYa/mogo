package io.github.loserya.function.replacer;


import io.github.loserya.utils.func.BoolFunction;

import java.lang.reflect.Method;

/**
 * 替换器 只会执行首个命中的替换器
 *
 * @author loser
 * @since 1.0.0
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