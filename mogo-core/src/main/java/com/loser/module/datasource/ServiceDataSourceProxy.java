package com.loser.module.datasource;

import com.loser.core.sdk.MogoService;
import com.loser.global.cache.MethodDsCache;
import com.loser.global.cache.MongoTemplateCache;
import com.loser.utils.StringUtils;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.Objects;

/**
 * 服务类数据源切换代理类
 *
 * @author loser
 * @date 2024/5/9
 */
@SuppressWarnings("all")
public class ServiceDataSourceProxy implements InvocationHandler {

    private MogoService target;

    public ServiceDataSourceProxy(MogoService target) {
        this.target = target;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

        String key = method.getDeclaringClass().getName() + "." + method.getName();
        String value = MethodDsCache.CACHE.get(key);
        if (Objects.isNull(value)) {
            value = getByAnnotation(method, key);
        }
        boolean notNull = StringUtils.isNotBlank(value);
        try {
            if (notNull) {
                MongoTemplateCache.setDataSource(value);
            }
            return method.invoke(target, args);
        } finally {
            if (notNull) {
                MongoTemplateCache.clear();
            }
        }

    }

    private String getByAnnotation(Method method, String key) throws NoSuchMethodException {

        String value;
        MongoDs annotation = target.getClass().getMethod(method.getName(), method.getParameterTypes()).getAnnotation(MongoDs.class);
        if (Objects.isNull(annotation)) {
            annotation = target.getClass().getAnnotation(MongoDs.class);
        }
        value = Objects.isNull(annotation) ? "" : annotation.value();
        MethodDsCache.CACHE.put(key, value);
        return value;

    }

}
