package io.github.loserya.config;

import io.github.loserya.function.interceptor.Interceptor;
import io.github.loserya.function.replacer.Replacer;
import io.github.loserya.global.cache.InterceptorCache;
import io.github.loserya.global.cache.MogoEnableCache;
import io.github.loserya.global.cache.MongoTemplateCache;
import io.github.loserya.global.cache.ReplacerCache;
import io.github.loserya.hardcode.constant.MogoConstant;
import io.github.loserya.module.logic.entity.LogicProperty;
import io.github.loserya.module.logic.interceptor.CollectionLogiceInterceptor;
import io.github.loserya.module.logic.interceptor.LogicAutoFillInterceptor;
import io.github.loserya.module.logic.replacer.LogicGetByIdReplacer;
import io.github.loserya.module.logic.replacer.LogicListByIdsReplacer;
import io.github.loserya.module.logic.replacer.LogicRemoveByIdReplacer;
import io.github.loserya.module.logic.replacer.LogicRemoveReplacer;
import io.github.loserya.module.logic.replacer.LogicUpdateByIdReplacer;
import io.github.loserya.utils.ExceptionUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.data.mongodb.core.MongoTemplate;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * 全局配置,单例
 *
 * @author loser
 * @date 2024/5/9
 */
public class MogoConfiguration implements ApplicationContextAware {

    private static final Log LOGGER = LogFactory.getLog(MogoConfiguration.class);

    private static final MogoConfiguration configuration = new MogoConfiguration();

    private LogicProperty logicProperty;

    private MogoConfiguration() {
    }

    public static MogoConfiguration instance() {
        return configuration;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {

        if (!MogoEnableCache.base) {
            return;
        }
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
        LOGGER.info(MogoConstant.LOG_PRE + String.format("mogo loadIocReplacers finish %s", Arrays.stream(replacers).map(i -> i.getClass().getName()).collect(Collectors.toList())));

    }

    /**
     * 加载容器类的拦截器
     */
    private void loadIocInterceptors(ApplicationContext applicationContext) {

        Interceptor[] interceptors = applicationContext.getBeansOfType(Interceptor.class).values().toArray(new Interceptor[0]);
        if (interceptors.length > 0) {
            interceptor(interceptors);
        }
        LOGGER.info(MogoConstant.LOG_PRE + String.format("mogo loadIocInterceptors finish %s", Arrays.stream(interceptors).map(i -> i.getClass().getName()).collect(Collectors.toList())));

    }

    public MogoConfiguration template(String name, MongoTemplate mongoTemplate) {
        MongoTemplateCache.CACHE.put(name, mongoTemplate);
        LOGGER.info(MogoConstant.LOG_PRE + String.format("mogo init MongoTemplate finish { %s : %s }", name, mongoTemplate));
        return this;
    }

    /**
     * 配置逻辑删除
     *
     * @param logicProperty 逻辑删除属性
     * @return 全局配置对象
     */
    public MogoConfiguration logic(LogicProperty logicProperty) {

        if (!MogoEnableCache.logic) {
            throw ExceptionUtils.mpe("mogo logic is unEnable");
        }
        if (Objects.nonNull(this.logicProperty)) {
            throw ExceptionUtils.mpe("logicProperty is config");
        }
        this.logicProperty = logicProperty;
        this.interceptor(CollectionLogiceInterceptor.class);
        this.replacer(
                LogicGetByIdReplacer.class,
                LogicListByIdsReplacer.class,
                LogicRemoveByIdReplacer.class,
                LogicRemoveReplacer.class,
                LogicUpdateByIdReplacer.class
        );
        if (MogoEnableCache.autoFill) {
            this.interceptor(LogicAutoFillInterceptor.class);
        }
        return this;

    }

    /**
     * 添加替换器
     */
    public final MogoConfiguration replacer(Replacer... replacers) {

        if (!MogoEnableCache.base) {
            throw ExceptionUtils.mpe("mogo base is unEnable");
        }
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

        if (!MogoEnableCache.base) {
            throw ExceptionUtils.mpe("mogo base is unEnable");
        }
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
        if (!MogoEnableCache.logic) {
            throw ExceptionUtils.mpe("mogo logic is unEnable");
        }
        return logicProperty;
    }

}
