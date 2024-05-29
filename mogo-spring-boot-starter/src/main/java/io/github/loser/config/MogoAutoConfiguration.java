package io.github.loser.config;

import io.github.loser.aspect.ds.MogoDSClassAspect;
import io.github.loser.aspect.ds.MogoDSMethodAspect;
import io.github.loser.aspect.ts.MogoTSClassAspect;
import io.github.loser.aspect.ts.MogoTSMethodAspect;
import io.github.loser.properties.MogoDataSourceProperties;
import io.github.loser.properties.MogoLogicProperties;
import io.github.loserya.config.MogoConfiguration;
import io.github.loserya.core.anno.EnableMogo;
import io.github.loserya.global.cache.MogoEnableCache;
import io.github.loserya.hardcode.constant.MogoConstant;
import io.github.loserya.utils.AnnotationUtil;
import io.github.loserya.utils.CollectionUtils;
import io.github.loserya.utils.ExceptionUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.core.annotation.Order;
import org.springframework.core.type.StandardAnnotationMetadata;
import org.springframework.data.mongodb.MongoDatabaseFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.convert.MongoConverter;

import java.lang.reflect.Field;
import java.util.Objects;
import java.util.Set;

/**
 * 基础必要配置
 *
 * @author loser
 * @since 1.0.0
 */
@EnableConfigurationProperties({MogoLogicProperties.class, MogoDataSourceProperties.class})
public class MogoAutoConfiguration {

    private static final Log LOGGER = LogFactory.getLog(MogoAutoConfiguration.class);

    @Bean
    public MogoDSClassAspect mogoDSClassAspect() {
        return new MogoDSClassAspect();
    }

    @Bean
    public MogoTSClassAspect mogoTSClassAspect() {
        return new MogoTSClassAspect();
    }

    @Bean
    public MogoDSMethodAspect mogoDSMethodAspect() {
        return new MogoDSMethodAspect();
    }

    @Bean
    public MogoTSMethodAspect mogoTSMethodAspect() {
        return new MogoTSMethodAspect();
    }

    @Bean
    @Order(Integer.MIN_VALUE)
    public MogoConfiguration mogoConfiguration(MongoTemplate mongoTemplate) {
        MogoConfiguration.instance().template(MogoConstant.MASTER_DS, mongoTemplate);
        return MogoConfiguration.instance();
    }

    public MogoAutoConfiguration(
            MongoConverter mongoConverter,
            ApplicationContext applicationContext,
            MogoLogicProperties mogoLogicProperties,
            MongoDatabaseFactory mongoDatabaseFactory,
            MogoDataSourceProperties mogoDataSourceProperties
    ) {
        // 01 开启功能
        enableFun(applicationContext);
        // 02 输出启动日志
        logBaseInfo();
        // 03 进行 mogo 初始化操作
        MogoInitializer.init(mogoLogicProperties, mongoDatabaseFactory, mogoDataSourceProperties, mongoConverter);
    }

    private static void enableFun(ApplicationContext applicationContext) {

        Set<String> beans = applicationContext.getBeansWithAnnotation(EnableMogo.class).keySet();
        if (CollectionUtils.isEmpty(beans)) {
            return;
        }
        for (String bean : beans) {
            try {
                BeanDefinition definition = ((BeanDefinitionRegistry) applicationContext).getBeanDefinition(bean);
                Field declaredField = definition.getClass().getDeclaredField("metadata");
                declaredField.setAccessible(true);
                Object metadata = declaredField.get(definition);
                EnableMogo enableMogo;
                if (metadata instanceof StandardAnnotationMetadata) {
                    enableMogo = AnnotationUtil.getAnnotation(((StandardAnnotationMetadata) metadata).getIntrospectedClass(), EnableMogo.class);
                } else {
                    enableMogo = AnnotationUtil.getAnnotation(Class.forName(definition.getBeanClassName()), EnableMogo.class);
                }
                if (Objects.nonNull(enableMogo)) {
                    MogoEnableCache.base = enableMogo.base();
                    MogoEnableCache.logic = enableMogo.logic();
                    MogoEnableCache.autoFill = enableMogo.autoFill();
                    MogoEnableCache.dynamicDs = enableMogo.dynamicDs();
                    MogoEnableCache.banFullTable = enableMogo.banFullTable();
                    MogoEnableCache.transaction = enableMogo.transaction();
                    MogoEnableCache.debugLog = enableMogo.debugLog();
                }
            } catch (Exception e) {
                throw ExceptionUtils.mpe("enableMogo @EnableMogo error", e);
            }
        }

    }

    private void logBaseInfo() {

        System.out.println(
                "  __  __    ____     _____    ____  \n" +
                        " |  \\/  |  / __ \\   / ____|  / __ \\ \n" +
                        " | \\  / | | |  | | | |  __  | |  | |\n" +
                        " | |\\/| | | |  | | | | |_ | | |  | |\n" +
                        " | |  | | | |__| | | |__| | | |__| |\n" +
                        " |_|  |_|  \\____/   \\_____|  \\____/"
        );
        System.out.println(":: Mogo starting ::           v1.1.4");
        System.out.println(":: gitee         ::           https://gitee.com/lyilan8080/mogo");
        System.out.println(":: doc           ::           https://loser.plus");
        System.out.println(":: author        ::           loser");
        System.out.println();
        if (MogoEnableCache.base) {
            LOGGER.info(MogoConstant.LOG_PRE + "mogo [base] switch is enable");
        } else {
            LOGGER.info(MogoConstant.LOG_PRE + "mogo [base] switch is unEnable");
        }
        if (MogoEnableCache.logic) {
            LOGGER.info(MogoConstant.LOG_PRE + "mogo [logic] switch is enable");
        } else {
            LOGGER.info(MogoConstant.LOG_PRE + "mogo [logic] switch is unEnable");
        }
        if (MogoEnableCache.autoFill) {
            LOGGER.info(MogoConstant.LOG_PRE + "mogo [logic autoFill] switch is enable");
        } else {
            LOGGER.info(MogoConstant.LOG_PRE + "mogo [logic autoFill] switch is unEnable");
        }
        if (MogoEnableCache.dynamicDs) {
            LOGGER.info(MogoConstant.LOG_PRE + "mogo [dynamicDs] switch is enable");
        } else {
            LOGGER.info(MogoConstant.LOG_PRE + "mogo [dynamicDs] switch is unEnable");
        }
        if (MogoEnableCache.banFullTable) {
            LOGGER.info(MogoConstant.LOG_PRE + "mogo [banFullTable] switch is enable");
        } else {
            LOGGER.info(MogoConstant.LOG_PRE + "mogo [banFullTable] switch is unEnable");
        }
        if (MogoEnableCache.transaction) {
            LOGGER.info(MogoConstant.LOG_PRE + "mogo [transaction] switch is enable");
        } else {
            LOGGER.info(MogoConstant.LOG_PRE + "mogo [transaction] switch is unEnable");
        }

    }


}
