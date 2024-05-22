package io.github.loser.aspect.ds;

import io.github.loserya.module.datasource.MongoDs;
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
public class MogoDSClassAspect extends DSAspect {

    @Around("execution(* *(..)) && @within(mongoDs)")
    public Object invoke(ProceedingJoinPoint joinPoint, MongoDs mongoDs) throws Throwable {
        return super.invoke(joinPoint, mongoDs);
    }

}
