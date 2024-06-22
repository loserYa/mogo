package io.github.loser.aspect;

import io.github.loserya.aop.ProxyUtil;
import io.github.loserya.aop.interceptor.MogoAopParams;
import io.github.loserya.aop.interceptor.MogoIntercept;
import io.github.loserya.core.sdk.impl.MogoServiceImpl;
import io.github.loserya.global.cache.MogoEnableCache;
import io.github.loserya.module.datasource.MongoDs;
import io.github.loserya.module.logic.IgnoreLogic;
import io.github.loserya.module.transaction.MogoTransaction;
import io.github.loserya.utils.AnnotationUtil;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.beans.factory.config.BeanPostProcessor;

/**
 * 判断是否是MogoServiceImpl并生成自定义代理类
 *
 * @author loser
 * @since 1.1.8
 */
public class MogoBeanProcessor implements BeanPostProcessor {

    private final AutowireCapableBeanFactory beanFactory;

    public MogoBeanProcessor(AutowireCapableBeanFactory beanFactory) {
        this.beanFactory = beanFactory;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {

        // 只针对 MogoServiceImpl 字类实现代理
        if (!(bean instanceof MogoServiceImpl)) {
            return bean;
        }
        // 如果 逻辑删除、多数据源、事务都没开启 则不生成代理对象
        if (!MogoEnableCache.logic && !MogoEnableCache.dynamicDs && !MogoEnableCache.transaction) {
            return bean;
        }
        Class<?> clazz = bean.getClass();
        MogoAopParams mogoAopParams = new MogoAopParams.Builder()
                .classAnnoDs(clazz.getAnnotation(MongoDs.class))
                .classAnnoTs(clazz.getAnnotation(MogoTransaction.class))
                .classAnnoIgnore(clazz.getAnnotation(IgnoreLogic.class))
                .methodMapperDs(AnnotationUtil.buildByClass(clazz, MongoDs.class))
                .methodMapperTs(AnnotationUtil.buildByClass(clazz, MogoTransaction.class))
                .methodMapperIgnore(AnnotationUtil.buildByClass(clazz, IgnoreLogic.class))
                .build();
        if (mogoAopParams.isIgnore()) {
            return bean;
        }
        // 手动走一遍依赖注入
        Object proxy = ProxyUtil.proxy(bean, new MogoIntercept(mogoAopParams));
        beanFactory.autowireBean(proxy);
        return proxy;

    }

}