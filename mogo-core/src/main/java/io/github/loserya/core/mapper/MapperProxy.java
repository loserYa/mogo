/**
 * MapperProxy.java 代码解读
 * 这段代码是一个Java类，名为MapperProxy，它实现了InvocationHandler接口，用于动态代理。这个类主要用于在调用数据库映射器（mapper）的方法时，进行一些额外的处理，如参数替换、方法替换等。下面是对代码的详细解释：
 * <p>
 * 类定义和构造函数：
 * <p>
 * MapperProxy类实现了InvocationHandler接口。
 * 构造函数接收一个BaseMapper类型的对象，并将其存储在target成员变量中。这个target是实际要被代理的对象。
 * invoke方法：
 * <p>
 * 这个方法是InvocationHandler接口的核心，它在代理对象上调用任何方法时被触发。
 * 方法接收三个参数：proxy（代理对象本身），method（被调用的方法），args（方法参数）。
 * 参数替换拦截器：
 * <p>
 * 首先，代码通过ExecutorProxyCache.EXECUTOR_MAP获取与当前方法名称对应的MethodExecutorStrategy。
 * 如果找到了对应的策略，它会遍历InterceptorCache.interceptors中的所有拦截器，并调用它们的invoke方法。
 * 方法替换执行器： - 接下来，如果当前方法不是Object类的方法（通过ClassUtil.isObjectMethod判断），代码会遍历ReplacerCache.replacers中的所有替换器。
 * <p>
 * 对于每个替换器，它检查是否应该替换当前方法的调用（通过调用replacer.supplier().get）。
 * 如果需要替换，它会调用替换器的invoke方法来执行替换逻辑。
 * 默认方法调用：
 * <p>
 * 如果没有找到任何替换器或拦截器，或者当前方法是Object类的方法，那么它将直接调用原始方法（method.invoke(target, args)）。
 * 注释和代码风格：
 * <p>
 * 类和方法都有详细的注释，说明了它们的用途和工作方式。
 * 总的来说，这个类是一个动态代理的实现，用于在调用数据库映射器的方法时，进行一些自定义的处理，如参数替换和方法替换。这种技术常用于框架开发中，以实现一些高级功能，如事务管理、权限检查、日志记录等。
 */
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
            InterceptorCache.interceptors.forEach(interceptor -> strategy.invoke(target.getTargetClass(), interceptor, args));
        }

        // 方法替换执行器 执行首个命中执行器
        if (!ClassUtil.isObjectMethod(method)) {
            for (Replacer replacer : ReplacerCache.replacers) {
                if (replacer.supplier().get(proxy, target, method, args)) {
                    return replacer.invoke(target.getTargetClass(), proxy, target, method, args);
                }
            }
        }

        return method.invoke(target, args);

    }

}
