package io.github.loser.aspect.ts;

import io.github.loserya.module.transaction.MogoTransaction;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.core.annotation.Order;

/**
 * 事务切面
 *
 * @author loser
 * @since 1.1.2
 */
@Aspect
@Order(0)
public class MogoTSMethodAspect extends TSAspect {

    @Around("@annotation(mogoTransaction)")
    public Object invoke(ProceedingJoinPoint joinPoint, MogoTransaction mogoTransaction) throws Throwable {
        return super.invoke(joinPoint, mogoTransaction);
    }

}
