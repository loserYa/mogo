package io.github.loserya.aop.proxy.impl;

import io.github.loserya.aop.interceptor.MogoIntercept;
import io.github.loserya.aop.proxy.ProxyFactory;
import net.bytebuddy.ByteBuddy;
import net.bytebuddy.implementation.MethodDelegation;
import net.bytebuddy.matcher.ElementMatchers;

/**
 * mogo 代理工厂lei
 *
 * @author loser
 * @since 1.1.8
 */
public class ByteBuddyProxyFactory extends ProxyFactory {

    @Override
    public <T> T proxy(T target, MogoIntercept intercept) {

        try {
            return (T) new ByteBuddy()
                    .subclass(target.getClass())
                    .method(ElementMatchers.any())
                    .intercept(MethodDelegation.to(intercept))
                    .make()
                    .load(ByteBuddyProxyFactory.class.getClassLoader())
                    .getLoaded()
                    .newInstance();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

}
