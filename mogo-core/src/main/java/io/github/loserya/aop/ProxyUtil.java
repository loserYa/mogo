package io.github.loserya.aop;

import io.github.loserya.aop.interceptor.MogoIntercept;
import io.github.loserya.aop.proxy.ProxyFactory;

/**
 * mogo 代理工具
 *
 * @author loser
 * @since 1.1.8
 */
public final class ProxyUtil {

    /**
     * 使用切面代理对象
     *
     * @param <T>    被代理对象类型
     * @param target 被代理对象
     * @param aspect 切面对象
     * @return 代理对象
     */
    public static <T> T proxy(T target, MogoIntercept aspect) {
        return ProxyFactory.createProxy(target, aspect);
    }

}
