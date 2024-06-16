/**
 * MogoConfiguration.java 代码解读
 * 这段代码是Java编写的，属于一个名为MogoConfiguration的类，这个类实现了ApplicationContextAware接口，用于在Spring框架中管理全局配置。以下是对代码的详细解释：
 * <p>
 * 1.类定义和单例模式：MogoConfiguration是一个单例类，它通过一个私有的构造函数和一个静态的实例来保证全局只有一个实例。
 * <p>
 * 2.日志记录：使用LogFactory来创建日志记录器，用于记录重要的信息和错误。
 * <p>
 * 3.实现ApplicationContextAware接口：通过实现这个接口，MogoConfiguration可以在Spring应用上下文准备好后执行一些初始化操作。
 * <p>
 * 4.初始化方法：setApplicationContext方法用于在Spring上下文准备好后进行初始化。它检查是否启用了Mogo，并加载各种处理器和拦截器。
 * <p>
 * 5.加载处理器和拦截器：
 * <p>
 * 5.1 loadIocInterceptors：加载拦截器。
 * 5.2 loadIocReplacers：加载替换器。
 * 5.3 loadIocMeatFilHandlers：加载对象字段填充处理器。
 * 5.4 loadIocIdGenStrategy：加载ID生成策略处理器。
 * 5.5 checkMustOneInterceptor：确保某些拦截器只有一个实例。
 * 6.配置方法：
 * <p>
 * 6.1 template和factory方法用于配置MongoTemplate和MongoDatabaseFactory。
 * 6.2 logic方法用于配置逻辑删除。
 * 6.3 filedFill、metaObjHandler、replacer、interceptor等方法用于添加各种处理器和拦截器。
 * 7.异常处理：使用ExceptionUtils来处理异常情况。
 * <p>
 * 8.缓存和排序：在添加处理器和拦截器时，会将它们存储在相应的缓存中，并进行排序。
 * <p>
 * 9.获取逻辑删除属性：getLogicProperty方法用于获取逻辑删除属性。
 * <p>
 * 整体来看，这个类是Mogo框架的核心配置类，负责管理和配置MongoDB相关的操作，如拦截器、替换器、字段填充处理器等。通过这种方式，Mogo框架提供了一种灵活的方式来扩展和定制MongoDB的操作。
 */
package io.github.loserya.config;

import io.github.loserya.function.interceptor.Interceptor;
import io.github.loserya.function.replacer.Replacer;
import io.github.loserya.global.cache.IdGenStrategyCache;
import io.github.loserya.global.cache.InterceptorCache;
import io.github.loserya.global.cache.MeatObjectCache;
import io.github.loserya.global.cache.MogoEnableCache;
import io.github.loserya.global.cache.MongoTemplateCache;
import io.github.loserya.global.cache.ReplacerCache;
import io.github.loserya.hardcode.constant.MogoConstant;
import io.github.loserya.module.fill.FieldFillHandler;
import io.github.loserya.module.fill.MetaObjectHandler;
import io.github.loserya.module.idgen.strategy.IdGenStrategy;
import io.github.loserya.module.interceptor.datapermission.DataPermissionInterceptor;
import io.github.loserya.module.interceptor.tenantline.TenantLineInterceptor;
import io.github.loserya.module.logic.entity.LogicProperty;
import io.github.loserya.module.logic.interceptor.CollectionLogiceInterceptor;
import io.github.loserya.module.logic.interceptor.LogicAutoFillInterceptor;
import io.github.loserya.module.logic.replacer.LogicRemoveReplacer;
import io.github.loserya.utils.ExceptionUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.data.mongodb.MongoDatabaseFactory;
import org.springframework.data.mongodb.MongoTransactionManager;
import org.springframework.data.mongodb.core.MongoTemplate;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 全局配置,单例
 *
 * @author loser
 * @since 1.0.0
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
        // 03 加载对象字段填充处理器
        loadIocMeatFilHandlers(applicationContext);
        // 04 加载ID生成策略处理器
        loadIocIdGenStrategy(applicationContext);
        // 05 检测只能单个的拦截器
        checkMustOneInterceptor(applicationContext, TenantLineInterceptor.class, DataPermissionInterceptor.class);

    }

    /**
     * 检测只能单个的拦截器
     */
    @SafeVarargs
    private final void checkMustOneInterceptor(ApplicationContext applicationContext, Class<? extends Interceptor>... classList) {

        for (Class<? extends Interceptor> clazz : classList) {
            Collection<? extends Interceptor> values = applicationContext.getBeansOfType(clazz).values();
            if (values.size() > 1) {
                throw ExceptionUtils.mpe(String.format("%s subClass must only one in IOC.", clazz.getSimpleName()));
            }
        }

    }

    /**
     * 加载ID生成策略处理器 可覆盖系统内置 id 生成器 建议只重写 CUSTOM
     */
    private void loadIocIdGenStrategy(ApplicationContext applicationContext) {

        // 添加全局
        IdGenStrategy[] strategies = applicationContext.getBeansOfType(IdGenStrategy.class).values().toArray(new IdGenStrategy[0]);
        if (strategies.length > 0) {
            idGenStrategy(strategies);
        }
        LOGGER.info(MogoConstant.LOG_PRE + String.format("mogo idStrategies finish %s", Arrays.stream(strategies).map(i -> i.getClass().getName()).collect(Collectors.toList())));


    }

    /**
     * 加载对象字段填充处理器
     */
    private void loadIocMeatFilHandlers(ApplicationContext applicationContext) {

        // 添加全局
        MetaObjectHandler[] metaObjectHandlers = applicationContext.getBeansOfType(MetaObjectHandler.class).values().toArray(new MetaObjectHandler[0]);
        if (metaObjectHandlers.length > 0) {
            metaObjHandler(metaObjectHandlers);
        }
        LOGGER.info(MogoConstant.LOG_PRE + String.format("mogo metaObjectHandlers finish %s", Arrays.stream(metaObjectHandlers).map(i -> i.getClass().getName()).collect(Collectors.toList())));

        // 添加单个字段
        FieldFillHandler<?>[] fillHandlers = applicationContext.getBeansOfType(FieldFillHandler.class).values().toArray(new FieldFillHandler[0]);
        if (fillHandlers.length > 0) {
            filedFill(fillHandlers);
        }
        LOGGER.info(MogoConstant.LOG_PRE + String.format("mogo fillHandlers finish %s", Arrays.stream(metaObjectHandlers).map(i -> i.getClass().getName()).collect(Collectors.toList())));

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

    public MogoConfiguration factory(String name, MongoDatabaseFactory mongoDatabaseFactory) {

        MongoTemplateCache.FACTORY.put(name, mongoDatabaseFactory);
        if (name.equals(MogoConstant.MASTER_DS)) {
            MongoTemplateCache.MANAGER.put(name, new MongoTransactionManager(mongoDatabaseFactory));
        } else {
            MongoTemplateCache.MANAGER.put(name, new MongoTransactionManager(mongoDatabaseFactory));
        }
        LOGGER.info(MogoConstant.LOG_PRE + String.format("mogo init MongoDatabaseFactory finish { %s : %s }", name, mongoDatabaseFactory));
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
        this.logicProperty = logicProperty;
        this.interceptor(CollectionLogiceInterceptor.class);
        this.replacer(LogicRemoveReplacer.class);
        if (MogoEnableCache.autoFill) {
            this.interceptor(LogicAutoFillInterceptor.class);
        }
        return this;

    }

    /**
     * 添加全局填充器
     */
    public final MogoConfiguration filedFill(FieldFillHandler<?>... handlers) {

        if (!MogoEnableCache.base) {
            throw ExceptionUtils.mpe("mogo base is unEnable");
        }
        for (FieldFillHandler<?> handler : handlers) {
            MeatObjectCache.HANDLER_MAP.put(handler.getClass(), handler);
        }
        return this;

    }

    /**
     * 添加替换器
     */
    @SafeVarargs
    public final MogoConfiguration filedFill(Class<? extends FieldFillHandler<?>>... handlers) {

        FieldFillHandler<?>[] metaObjectHandlers = Arrays.stream(handlers).map(item -> {
            try {
                return item.newInstance();
            } catch (Exception e) {
                throw ExceptionUtils.mpe(e);
            }
        }).toArray(FieldFillHandler[]::new);
        return filedFill(metaObjectHandlers);

    }

    /**
     * 添加 id 生成器
     */
    public final MogoConfiguration idGenStrategy(IdGenStrategy... handlers) {

        if (!MogoEnableCache.base) {
            throw ExceptionUtils.mpe("mogo base is unEnable");
        }
        for (IdGenStrategy handler : handlers) {
            IdGenStrategyCache.MAP.put(handler.getType(), handler);
        }
        return this;

    }

    /**
     * 添加 id 生成器
     */
    @SafeVarargs
    public final MogoConfiguration idGenStrategy(Class<? extends IdGenStrategy>... handlers) {

        IdGenStrategy[] strategies = Arrays.stream(handlers).map(item -> {
            try {
                return item.newInstance();
            } catch (Exception e) {
                throw ExceptionUtils.mpe(e);
            }
        }).toArray(IdGenStrategy[]::new);
        return idGenStrategy(strategies);

    }

    /**
     * 添加全局填充器
     */
    public final MogoConfiguration metaObjHandler(MetaObjectHandler... handlers) {

        if (!MogoEnableCache.base) {
            throw ExceptionUtils.mpe("mogo base is unEnable");
        }
        MeatObjectCache.handlers.addAll(Arrays.asList(handlers));
        MeatObjectCache.sorted();
        return this;

    }

    /**
     * 添加替换器
     */
    @SafeVarargs
    public final MogoConfiguration metaObjHandler(Class<? extends MetaObjectHandler>... handlers) {

        MetaObjectHandler[] metaObjectHandlers = Arrays.stream(handlers).map(item -> {
            try {
                return item.newInstance();
            } catch (Exception e) {
                throw ExceptionUtils.mpe(e);
            }
        }).toArray(MetaObjectHandler[]::new);
        return metaObjHandler(metaObjectHandlers);

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
