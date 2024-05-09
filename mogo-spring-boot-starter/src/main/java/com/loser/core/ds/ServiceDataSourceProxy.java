package com.loser.core.ds;

import com.loser.core.cache.global.MongoTemplateCache;
import com.loser.core.sdk.MogoService;

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

        MongoDs annotation = target.getClass().getMethod(method.getName(), method.getParameterTypes()).getAnnotation(MongoDs.class);
        if (Objects.isNull(annotation)) {
            annotation = target.getClass().getAnnotation(MongoDs.class);
        }
        boolean notNull = Objects.nonNull(annotation);
        try {
            if (notNull) {
                MongoTemplateCache.setDataSource(annotation.value());
            }
            return method.invoke(target, args);
        } finally {
            if (notNull) {
                MongoTemplateCache.clear();
            }
        }

    }

}
