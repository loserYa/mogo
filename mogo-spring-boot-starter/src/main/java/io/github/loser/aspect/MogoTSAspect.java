package io.github.loser.aspect;

import io.github.loserya.global.cache.MogoEnableCache;
import io.github.loserya.global.cache.MongoTemplateCache;
import io.github.loserya.utils.ExceptionUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.core.annotation.Order;
import org.springframework.transaction.support.TransactionTemplate;

/**
 * 事务切面
 *
 * @author loser
 * @since 1.1.2
 */
@Aspect
@Order(0)
public class MogoTSAspect {

    @Around("@annotation(io.github.loserya.module.transaction.MogoTransaction)")
    public Object manageTransaction(ProceedingJoinPoint joinPoint) throws Throwable {

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
