package io.github.loserya.function.executor;


import io.github.loserya.function.interceptor.Interceptor;
import io.github.loserya.hardcode.constant.ExecuteMethodEnum;
import io.github.loserya.utils.ExceptionUtils;

/**
 * 方法执行策略(解耦逻辑)
 *
 * @author loser
 * @date 2024/4/28
 */
public interface MethodExecutorStrategy {

    /**
     * 方法类型
     */
    ExecuteMethodEnum method();

    /**
     * 执行拦截方法
     */
    void invoke(Class<?> clazz, Interceptor interceptor, Object[] args);

    default void replace(Object[] oldArgs, Object[] newArgs) {

        if (oldArgs.length != newArgs.length) {
            throw ExceptionUtils.mpe("args length not match");
        }
        System.arraycopy(newArgs, 0, oldArgs, 0, oldArgs.length);

    }

}
