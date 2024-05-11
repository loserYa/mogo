package io.github.loserya.core.mapper;

import io.github.loserya.core.sdk.mapper.BaseMapper;
import io.github.loserya.function.executor.MethodExecutorStrategy;
import io.github.loserya.function.replacer.Replacer;
import io.github.loserya.global.cache.ExecutorProxyCache;
import io.github.loserya.global.cache.InterceptorCache;
import io.github.loserya.global.cache.ReplacerCache;
import io.github.loserya.utils.ClassUtil;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.Objects;

/**
 * mapper 代理类
 *
 * @author loser
 * @since 1.0.0 2024/5/8
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
