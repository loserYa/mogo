package com.loser.config;

import com.loser.function.interceptor.Interceptor;
import com.loser.function.replacer.Replacer;
import com.loser.global.cache.CollectionLogicDeleteCache;
import com.loser.global.cache.InterceptorCache;
import com.loser.global.cache.MongoTemplateCache;
import com.loser.global.cache.ReplacerCache;
import com.loser.module.logic.entity.LogicProperty;
import com.loser.module.logic.interceptor.CollectionLogiceInterceptor;
import com.loser.module.logic.interceptor.LogicAutoFillInterceptor;
import com.loser.module.logic.replacer.LogicGetByIdReplacer;
import com.loser.module.logic.replacer.LogicListByIdsReplacer;
import com.loser.module.logic.replacer.LogicRemoveByIdReplacer;
import com.loser.module.logic.replacer.LogicRemoveReplacer;
import com.loser.module.logic.replacer.LogicUpdateByIdReplacer;
import com.loser.utils.ExceptionUtils;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.data.mongodb.core.MongoTemplate;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * 全局配置,单例
 *
 * @author loser
 * @date 2024/5/9
 */
public class MogoConfiguration implements ApplicationContextAware {

    private static final MogoConfiguration configuration = new MogoConfiguration();

    private LogicProperty logicProperty;

    private MogoConfiguration() {
    }

    public static MogoConfiguration instance() {
        return configuration;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {

        // 01 加载容器类的拦截器
        loadIocInterceptors(applicationContext);
        // 02 加载容器内的替换器
        loadIocReplacers(applicationContext);

    }

    /**
     * 加载容器内的替换器
     */
    private void loadIocReplacers(ApplicationContext applicationContext) {

        Replacer[] replacers = applicationContext.getBeansOfType(Replacer.class).values().toArray(new Replacer[0]);
        if (replacers.length > 0) {
            replacer(replacers);
        }

    }

    /**
     * 加载容器类的拦截器
     */
    private void loadIocInterceptors(ApplicationContext applicationContext) {

        Interceptor[] interceptors = applicationContext.getBeansOfType(Interceptor.class).values().toArray(new Interceptor[0]);
        if (interceptors.length > 0) {
            interceptor(interceptors);
        }

    }

    public MogoConfiguration template(String name, MongoTemplate mongoTemplate) {
        MongoTemplateCache.CACHE.put(name, mongoTemplate);
        return this;
    }

    /**
     * 配置逻辑删除
     *
     * @param logicProperty 逻辑删除属性
     * @return 全局配置对象
     */
    public MogoConfiguration logic(LogicProperty logicProperty) {

        if (Objects.nonNull(this.logicProperty)) {
            throw ExceptionUtils.mpe("logicProperty is config");
        }
        this.logicProperty = logicProperty;
        CollectionLogicDeleteCache.open = logicProperty.getOpen();
        if (logicProperty.getOpen()) {
            this.interceptor(CollectionLogiceInterceptor.class);
            if (logicProperty.getAutoFill()) {
                this.interceptor(LogicAutoFillInterceptor.class);
            }
            this.replacer(
                    LogicGetByIdReplacer.class,
                    LogicListByIdsReplacer.class,
                    LogicRemoveByIdReplacer.class,
                    LogicRemoveReplacer.class,
                    LogicUpdateByIdReplacer.class
            );
        }
        return this;

    }

    /**
     * 添加替换器
     */
    public final MogoConfiguration replacer(Replacer... replacers) {

        List<Replacer> replacersCache = ReplacerCache.replacers;
        replacersCache.addAll(Arrays.asList(replacers));
        ReplacerCache.sorted();
        return this;

    }

    /**
     * 添加替换器
     */
    @SafeVarargs
    public final MogoConfiguration replacer(Class<? extends Replacer>... replacers) {

        Replacer[] replacersList = Arrays.stream(replacers).map(item -> {
            try {
                return item.newInstance();
            } catch (Exception e) {
                throw ExceptionUtils.mpe(e);
            }
        }).toArray(Replacer[]::new);
        return replacer(replacersList);

    }

    /**
     * 添加拦截器
     */
    public final MogoConfiguration interceptor(Interceptor... interceptors) {

        List<Interceptor> interceptorsCache = InterceptorCache.interceptors;
        interceptorsCache.addAll(Arrays.asList(interceptors));
        InterceptorCache.sorted();
        return this;

    }

    /**
     * 添加拦截器
     */
    @SafeVarargs
    public final MogoConfiguration interceptor(Class<? extends Interceptor>... interceptors) {

        Interceptor[] replacersList = Arrays.stream(interceptors).map(item -> {
            try {
                return item.newInstance();
            } catch (Exception e) {
                throw ExceptionUtils.mpe(e);
            }
        }).toArray(Interceptor[]::new);
        return interceptor(replacersList);

    }

    public LogicProperty getLogicProperty() {
        return logicProperty;
    }

}
