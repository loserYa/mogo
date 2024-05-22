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
