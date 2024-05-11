package io.github.loserya.utils.checker;

/**
 * 参数校验工具
 *
 * @author loser
 * @since 1.0.0
 */
public class Checkers {

    private Checkers() {
    }

    public static <T> Checker<T> lambdaCheck() {
        return new Checker<>();
    }

}
