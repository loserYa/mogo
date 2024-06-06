/**
 * DSAspect.java 代码解读
 * 这段代码是一个Java类，名为DSAspect，它位于io.github.loser.aspect.ds包中。这个类主要用于处理数据库连接的动态切换。下面是对这段代码的详细解释：
 * <p>
 * DSAspect类：
 * <p>
 * 这个类定义了一个方法invoke，该方法是一个切面方法，用于拦截被注解的方法调用。
 * invoke方法：
 * <p>
 * 参数：ProceedingJoinPoint joinPoint（表示当前被拦截的方法）和MongoDs mongoDs（表示注解的数据源名称）。
 * 首先检查MogoEnableCache.dynamicDs是否为false，如果是，则直接继续执行原方法，不进行数据源切换。
 * 获取当前缓存的数据源名称lastDs。
 * 检查mongoDs是否为null，如果不是，则获取其值value。
 * 如果value不为空，则将其设置为当前数据源。
 * 执行原方法。
 * 在finally块中，如果之前设置了新的数据源，则恢复原来的数据源，或者清除数据源缓存。
 * 功能：
 * <p>
 * 这个类的主要功能是在运行时根据方法参数动态切换MongoDB的数据源。这在多数据库环境下非常有用，可以根据不同的业务需求连接到不同的数据库。
 * 使用场景：
 * <p>
 * 在一个应用程序中，如果需要根据不同的业务逻辑连接到不同的MongoDB数据库，可以使用这个切面来动态切换数据源。
 * 注意事项：
 * <p>
 * 这个类依赖于AspectJ和Spring AOP，因此需要在项目中正确配置这些依赖。
 * 动态数据源切换可能会对性能产生影响，特别是在高并发的环境下。
 */
package io.github.loser.aspect.ds;

import io.github.loserya.global.cache.MogoEnableCache;
import io.github.loserya.global.cache.MongoTemplateCache;
import io.github.loserya.hardcode.constant.MogoConstant;
import io.github.loserya.module.datasource.MongoDs;
import io.github.loserya.utils.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;

import java.util.Objects;

public class DSAspect {

    public Object invoke(ProceedingJoinPoint joinPoint, MongoDs mongoDs) throws Throwable {

        if (!MogoEnableCache.dynamicDs) {
            return joinPoint.proceed();
        }
        String lastDs = MongoTemplateCache.getDataSource();
        String value = Objects.isNull(mongoDs) ? null : mongoDs.value();
        boolean notNull = StringUtils.isNotBlank(value);
        try {
            if (notNull) {
                MongoTemplateCache.setDataSource(value);
            }
            return joinPoint.proceed();
        } finally {
            if (notNull) {
                if (StringUtils.isNotBlank(lastDs) && !MogoConstant.MASTER_DS.equals(lastDs)) {
                    MongoTemplateCache.setDataSource(lastDs);
                } else {
                    MongoTemplateCache.clear();
                }
            }
        }

    }

}
