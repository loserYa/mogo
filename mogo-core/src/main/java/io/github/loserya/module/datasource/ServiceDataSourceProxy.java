package io.github.loserya.module.datasource;

import io.github.loserya.core.proxy.MogoProxy;
import io.github.loserya.global.cache.MethodDsCache;
import io.github.loserya.global.cache.MongoTemplateCache;
import io.github.loserya.utils.ExceptionUtils;
import io.github.loserya.utils.StringUtils;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Objects;

/**
 * 服务类数据源切换代理类
 *
 * @author loser
 * @since 1.0.0
 */
@SuppressWarnings("all")
public class ServiceDataSourceProxy extends MogoProxy {

    public ServiceDataSourceProxy(Object target) {
        super(target);
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

        String key = method.getDeclaringClass().getName() + "." + method.getName();
        String value = MethodDsCache.CACHE.get(key);
        if (Objects.isNull(value)) {
            Class<?> clazz = getUnProxyClass(getTarget());
            value = getByAnnotation(clazz.getMethod(method.getName(), method.getParameterTypes()), key);
        }
        boolean notNull = StringUtils.isNotBlank(value);
        try {
            if (notNull) {
                MongoTemplateCache.setDataSource(value);
            }
            return method.invoke(getTarget(), args);
        } finally {
            if (notNull) {
                MongoTemplateCache.clear();
            }
        }

    }

    private Class<?> getUnProxyClass(Object target) {

        if (!Proxy.isProxyClass(target.getClass())) {
            return target.getClass();
        }
        InvocationHandler handler = Proxy.getInvocationHandler(target);
        if (handler instanceof MogoProxy) {
            return getUnProxyClass(((MogoProxy) handler).getTarget());
        } else {
            throw ExceptionUtils.mpe("Unknown proxy handler type");
        }

    }

    private String getByAnnotation(Method method, String key) throws NoSuchMethodException {

        String value;
        MongoDs annotation = method.getAnnotation(MongoDs.class);
        if (Objects.isNull(annotation)) {
            annotation = method.getDeclaringClass().getAnnotation(MongoDs.class);
        }
        value = Objects.isNull(annotation) ? "" : annotation.value();
        MethodDsCache.CACHE.put(key, value);
        return value;

    }

}
