package io.github.loserya.aop.proxy;

import io.github.loserya.aop.interceptor.MogoIntercept;
import io.github.loserya.aop.proxy.impl.ByteBuddyProxyFactory;

/**
 * mogo 代理工厂
 *
 * @author loser
 * @since 1.1.8
 */
public abstract class ProxyFactory {

    /**
     * 创建代理
     *
     * @param <T>       代理对象类型
     * @param target    被代理对象
     * @param intercept 切面实现
     * @return 代理对象
     */
    public abstract <T> T proxy(T target, MogoIntercept intercept);

    /**
     * 根据用户引入Cglib与否自动创建代理对象
     *
     * @param <T>       切面对象类型
     * @param target    被代理对象
     * @param intercept 切面实现
     * @return 代理对象
     */
    public static <T> T createProxy(T target, MogoIntercept intercept) {
        return create().proxy(target, intercept);
    }

    /**
     * 根据用户引入Cglib与否创建代理工厂
     *
     * @return 代理工厂
     */
    public static ProxyFactory create() {
        return new ByteBuddyProxyFactory();
    }
}
