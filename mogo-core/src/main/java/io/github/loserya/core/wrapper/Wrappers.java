package io.github.loserya.core.wrapper;

/**
 * Wrapper 条件构造
 *
 * @author Caratacus
 */
public final class Wrappers {

    public static <T> LambdaQueryWrapper<T> lambdaQuery() {
        return empty();
    }

    public static <T> LambdaQueryWrapper<T> empty() {
        return new LambdaQueryWrapper<>();
    }

}
