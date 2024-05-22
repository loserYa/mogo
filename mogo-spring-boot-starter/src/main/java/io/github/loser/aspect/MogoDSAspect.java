package io.github.loser.aspect;

import io.github.loserya.global.cache.MogoEnableCache;
import io.github.loserya.global.cache.MongoTemplateCache;
import io.github.loserya.hardcode.constant.MogoConstant;
import io.github.loserya.module.datasource.MongoDs;
import io.github.loserya.utils.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.core.annotation.Order;

/**
 * 数据源切面
 *
 * @author loser
 * @since 1.1.2
 */
@Aspect
@Order(0)
public class MogoDSAspect {

    @Around("execution(* *(..)) && @within(mongoDs)")
    public Object manageDataSource(ProceedingJoinPoint joinPoint, MongoDs mongoDs) throws Throwable {

        if (!MogoEnableCache.dynamicDs) {
            return joinPoint.proceed();
        }
        String lastDs = MongoTemplateCache.getDataSource();
        String value = mongoDs.value();
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
