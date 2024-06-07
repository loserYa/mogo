package io.github.loser.aspect.logic;

import io.github.loserya.module.logic.IgnoreLogic;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.core.annotation.Order;


@Aspect
@Order(0)
public class IgnoreLogicClassAspect extends IgnoreLogicAspect {

    @Around("execution(* *(..)) && @within(ignoreLogic)")
    public Object invoke(ProceedingJoinPoint joinPoint, IgnoreLogic ignoreLogic) throws Throwable {
        return super.invoke(joinPoint, ignoreLogic);
    }

}
