package io.github.loserya.aop.interceptor;

import io.github.loserya.global.cache.CollectionLogicDeleteCache;
import io.github.loserya.global.cache.MogoEnableCache;
import io.github.loserya.global.cache.MongoTemplateCache;
import io.github.loserya.hardcode.constant.MogoConstant;
import io.github.loserya.module.datasource.MongoDs;
import io.github.loserya.utils.ExceptionUtils;
import io.github.loserya.utils.StringUtils;
import net.bytebuddy.implementation.bind.annotation.Origin;
import net.bytebuddy.implementation.bind.annotation.RuntimeType;
import net.bytebuddy.implementation.bind.annotation.SuperCall;
import org.springframework.transaction.support.TransactionTemplate;

import java.lang.reflect.Method;
import java.util.Objects;
import java.util.concurrent.Callable;

/**
 * mogo 切面拦截器
 *
 * @author loser
 * @since 1.1.8
 */
public class MogoIntercept {

    private final MogoAopParams mogoAopParams;

    public MogoIntercept(MogoAopParams mogoAopParams) {
        this.mogoAopParams = mogoAopParams;
    }

    @RuntimeType
    public Object intercept(@Origin Method method, @SuperCall Callable<?> callable) throws Exception {
        return doInvokeByDs(method, callable);
    }

    /**
     * 执行多数据标记逻辑
     */
    private Object doInvokeByDs(Method method, Callable<?> callable) throws Exception {

        String ds = isUseDs(method);
        if (StringUtils.isEmpty(ds)) {
            return doInvokeByIgnore(method, callable);
        }
        String lastDs = MongoTemplateCache.getDataSource();
        try {
            MongoTemplateCache.setDataSource(ds);
            return doInvokeByIgnore(method, callable);
        } finally {
            if (StringUtils.isNotBlank(lastDs) && !MogoConstant.MASTER_DS.equals(lastDs)) {
                MongoTemplateCache.setDataSource(lastDs);
            } else {
                MongoTemplateCache.clear();
            }
        }

    }

    /**
     * 执行逻辑删除忽略标记逻辑
     */
    private Object doInvokeByIgnore(Method method, Callable<?> callable) throws Exception {

        boolean useIgnore = isUseIgnore(method);
        if (!useIgnore) {
            return doByOpenTs(method, callable);
        }
        try {
            CollectionLogicDeleteCache.setIgnoreLogic(true);
            return callable.call();
        } finally {
            CollectionLogicDeleteCache.clear();
        }

    }

    /**
     * 执行事务提交逻辑
     */
    private Object doByOpenTs(Method method, Callable<?> callable) throws Exception {

        boolean useTs = isUseTs(method);
        if (!useTs) {
            return callable.call();
        }
        TransactionTemplate transactionTemplate = new TransactionTemplate(MongoTemplateCache.getManager());
        return transactionTemplate.execute(handler -> {
            try {
                return callable.call();
            } catch (Throwable e) {
                // 标记事务回滚
                handler.setRollbackOnly();
                throw ExceptionUtils.mpe(e);
            }
        });

    }

    /**
     * 类或者方法上存在则开启事务
     */
    private boolean isUseTs(Method method) {

        if (!MogoEnableCache.transaction) {
            return false;
        }
        MogoAopParams.MogoTsParams params = mogoAopParams.getMogoTsParams();
        return Objects.nonNull(params.getClassAnno()) || Objects.nonNull(params.getMethodMapper().get(method));

    }

    /**
     * 类或者方法存在责忽略逻辑删除
     */
    private boolean isUseIgnore(Method method) {

        if (!MogoEnableCache.logic) {
            return false;
        }
        MogoAopParams.MogoIgnoreLogicParams params = mogoAopParams.getMogoIgnoreLogicParams();
        return Objects.nonNull(params.getClassAnno()) || Objects.nonNull(params.getMethodMapper().get(method));

    }

    /**
     * 类或者方法上存在注解则返回对应的数据源
     */
    private String isUseDs(Method method) {

        if (!MogoEnableCache.dynamicDs) {
            return "";
        }
        MogoAopParams.MogoDsParams params = mogoAopParams.getMogoDsParams();
        MongoDs mongoDs = params.getMethodMapper().get(method);
        if (Objects.nonNull(mongoDs)) {
            return mongoDs.value();
        }
        mongoDs = params.getClassAnno();
        if (Objects.nonNull(mongoDs)) {
            return mongoDs.value();
        }
        return "";

    }

}
