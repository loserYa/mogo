/**
 * TSAspect.java 代码解读
 * 这段代码是一个Java类，名为TSAspect，它实现了一个事务切面（Aspect），用于在Spring框架中管理事务。这个类是mogo-spring-boot-starter项目的一部分，该项目似乎是为了在使用MongoDB时提供Spring Boot支持。下面是对这段代码的详细解释：
 * <p>
 * 事务管理逻辑：
 * <p>
 * 首先检查MogoEnableCache.transaction是否为false。如果是，直接继续执行joinPoint.proceed()，这意味着不涉及事务处理。
 * 如果涉及事务处理，则创建一个新的TransactionTemplate实例，使用MongoTemplateCache.getManager()作为事务管理器。
 * 使用TransactionTemplate的execute方法来执行事务。在这个方法的lambda表达式中，尝试执行joinPoint.proceed()。
 * 如果在执行过程中抛出异常，则通过handler.setRollbackOnly()标记事务为回滚，并重新抛出异常。
 * 异常处理：
 * <p>
 * 使用ExceptionUtils.mpe(e)来处理异常。这可能是一个自定义的异常处理方法，用于包装原始异常。
 * 注释： 类和方法都有详细的注释，说明了它们的用途和工作方式。
 * <p>
 * 总的来说，这个TSAspect类是一个用于处理MongoDB事务的Spring AOP切面。它通过Spring的事务模板（TransactionTemplate）来管理事务的开始、提交和回滚。
 */
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
