package io.github.loser.aspect.ts;

import io.github.loserya.global.cache.MogoEnableCache;
import io.github.loserya.global.cache.MongoTemplateCache;
import io.github.loserya.module.transaction.MogoTransaction;
import io.github.loserya.utils.ExceptionUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.springframework.transaction.support.TransactionTemplate;

/**
 * 事务切面
 *
 * @author loser
 * @since 1.1.2
 */
public class TSAspect {

    public Object invoke(ProceedingJoinPoint joinPoint, MogoTransaction mogoTransaction) throws Throwable {

        if (!MogoEnableCache.transaction) {
            return joinPoint.proceed();
        }
        TransactionTemplate transactionTemplate = new TransactionTemplate(MongoTemplateCache.getManager());
        return transactionTemplate.execute(handler -> {
            try {
                return joinPoint.proceed();
            } catch (Throwable e) {
                // 标记事务回滚
                handler.setRollbackOnly();
                throw ExceptionUtils.mpe(e);
            }
        });

    }

}
