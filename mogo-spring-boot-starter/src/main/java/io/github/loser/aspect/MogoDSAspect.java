package io.github.loser.aspect;

import io.github.loserya.core.proxy.MogoProxy;
import io.github.loserya.global.cache.MethodDsCache;
import io.github.loserya.global.cache.MogoEnableCache;
import io.github.loserya.global.cache.MongoTemplateCache;
import io.github.loserya.module.datasource.MongoDs;
import io.github.loserya.utils.ExceptionUtils;
import io.github.loserya.utils.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.annotation.Order;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Objects;

/**
 * 数据源切面
 *
 * @author loser
 * @since 1.1.2
 */
@Aspect
@Order(0)
public class MogoDSAspect {

    @Around("@annotation(io.github.loserya.module.datasource.MongoDs)")
    public Object manageDataSource(ProceedingJoinPoint joinPoint) throws Throwable {

        if (!MogoEnableCache.dynamicDs) {
            return joinPoint.proceed();
        }
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        String key = method.getDeclaringClass().getName() + "." + method.getName();
        String value = MethodDsCache.CACHE.get(key);
        if (Objects.isNull(value)) {
            Object target = joinPoint.getTarget();
            Class<?> clazz = getUnProxyClass(target);
            value = getByAnnotation(clazz.getMethod(method.getName(), method.getParameterTypes()), key);
        }
        boolean notNull = StringUtils.isNotBlank(value);
        try {
            if (notNull) {
                MongoTemplateCache.setDataSource(value);
            }
            return joinPoint.proceed();
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

    private String getByAnnotation(Method method, String key) {

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
