package com.loser.utils.func;


import java.lang.reflect.Method;

/**
 * boolean function
 *
 * @author loser
 * @date 2024/4/30
 */
@FunctionalInterface
public interface BoolFunction {

    boolean get(Object proxy, Object target, Method method, Object[] args);

}
