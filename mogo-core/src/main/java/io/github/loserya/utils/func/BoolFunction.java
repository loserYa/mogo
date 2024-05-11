package io.github.loserya.utils.func;


import java.lang.reflect.Method;

/**
 * boolean function
 *
 * @author loser
 * @since 1.0.0
 */
@FunctionalInterface
public interface BoolFunction {

    boolean get(Object proxy, Object target, Method method, Object[] args);

}
