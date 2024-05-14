package io.github.loserya.module.transaction;

import io.github.loserya.core.proxy.MogoProxy;
import io.github.loserya.global.cache.MongoTemplateCache;
import io.github.loserya.utils.ExceptionUtils;
import org.springframework.transaction.support.TransactionTemplate;

import java.lang.reflect.Method;

@SuppressWarnings("all")
public class ServiceTransactionProxy extends MogoProxy {

    public ServiceTransactionProxy(Object target) {
        super(target);
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

        TransactionTemplate transactionTemplate = new TransactionTemplate(MongoTemplateCache.getManager());
        return transactionTemplate.execute(handler -> {
            try {
                return method.invoke(getTarget(), args);
            } catch (Exception e) {
                // 标记事务回滚
                handler.setRollbackOnly();
                throw ExceptionUtils.mpe(e);
            }
        });

    }


}
