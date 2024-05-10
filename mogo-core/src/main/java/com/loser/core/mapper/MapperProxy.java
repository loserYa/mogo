package com.loser.core.mapper;

import com.loser.core.sdk.mapper.BaseMapper;
import com.loser.function.executor.MethodExecutorStrategy;
import com.loser.function.replacer.Replacer;
import com.loser.global.cache.ExecutorProxyCache;
import com.loser.global.cache.InterceptorCache;
import com.loser.global.cache.ReplacerCache;
import com.loser.utils.ClassUtil;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.Objects;

/**
 * mapper 代理类
 *
 * @author loser
 * @date 2024/5/8
 */
@SuppressWarnings("all")
public class MapperProxy implements InvocationHandler {

    private final BaseMapper target;

    public MapperProxy(BaseMapper target) {
        this.target = target;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

        String name = method.getName();

        // 参数替换拦截器
        MethodExecutorStrategy strategy = ExecutorProxyCache.EXECUTOR_MAP.get(name);
        if (Objects.nonNull(strategy)) {
            InterceptorCache.interceptors.forEach(interceptor -> strategy.invoke(target.getTragetClass(), interceptor, args));
        }

        // 方法替换执行器 执行首个命中执行器
        if (!ClassUtil.isObjectMethod(method)) {
            for (Replacer replacer : ReplacerCache.replacers) {
                if (replacer.supplier().get(proxy, target, method, args)) {
                    return replacer.invoke(target.getTragetClass(), proxy, target, method, args);
                }
            }
        }

        return method.invoke(target, args);

    }

}
