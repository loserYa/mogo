package io.github.loser.aspect.logic;

import io.github.loserya.global.cache.CollectionLogicDeleteCache;
import io.github.loserya.global.cache.MogoEnableCache;
import io.github.loserya.module.logic.IgnoreLogic;
import org.aspectj.lang.ProceedingJoinPoint;

/**
 * 逻辑删除忽略切面
 *
 * @author loser
 * @since 1.1.5
 */
public class IgnoreLogicAspect {

    public Object invoke(ProceedingJoinPoint joinPoint, IgnoreLogic ignoreLogic) throws Throwable {

        if (!MogoEnableCache.logic) {
            return joinPoint.proceed();
        }
        try {
            CollectionLogicDeleteCache.setIgnoreLogic(true);
            return joinPoint.proceed();
        } finally {
            CollectionLogicDeleteCache.clear();
        }

    }

}
