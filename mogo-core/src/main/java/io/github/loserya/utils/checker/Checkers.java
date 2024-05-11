package io.github.loserya.utils.checker;

/**
 * 参数校验工具
 *
 * @author loser
 * @date 2022/7/4 15:05
 */
public class Checkers {

    private Checkers() {
    }

    public static <T> Checker<T> lambdaCheck() {
        return new Checker<>();
    }

}
