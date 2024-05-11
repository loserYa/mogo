package io.github.loser.config;

import io.github.loser.properties.MogoDataSourceProperties;
import io.github.loser.properties.MogoLogicProperties;
import io.github.loserya.config.MogoConfiguration;
import io.github.loserya.core.anno.EnableMogo;
import io.github.loserya.global.cache.MogoEnableCache;
import io.github.loserya.hardcode.constant.MogoConstant;
import io.github.loserya.utils.AnnotationUtil;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.core.annotation.Order;
import org.springframework.core.env.Environment;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * 基础必要配置
 *
 * @author loser
 * @date 2024/5/10
 */
@EnableConfigurationProperties({MogoLogicProperties.class, MogoDataSourceProperties.class})
public class MogoBaseConfiguration {

    private static final Log LOGGER = LogFactory.getLog(MogoBaseConfiguration.class);
    private final MongoTemplate mongoTemplate;

    public MogoBaseConfiguration(
            ApplicationContext applicationContext,
            MongoTemplate mongoTemplate,
            Environment environment,
            MogoDataSourceProperties mogoDataSourceProperties,
            MogoLogicProperties mogoLogicProperties
    ) {
        enableFun(applicationContext);
        logBaseInfo();
        this.mongoTemplate = mongoTemplate;
        if (MogoEnableCache.dynamicDs) {
            new MogoAutoConfiguration(environment, mogoLogicProperties, mogoDataSourceProperties);
        }

    }

    private static void enableFun(ApplicationContext applicationContext) {

        List<Object> beans = new ArrayList<>(applicationContext.getBeansWithAnnotation(EnableMogo.class).values());
        if (CollectionUtils.isEmpty(beans)) {
            return;
        }
        Object o = beans.get(0);
        EnableMogo enableMogo = AnnotationUtil.getAnnotation(o.getClass(), EnableMogo.class);
        if (Objects.nonNull(enableMogo)) {
            MogoEnableCache.base = enableMogo.base();
            MogoEnableCache.logic = enableMogo.logic();
            MogoEnableCache.autoFill = enableMogo.autoFill();
            MogoEnableCache.dynamicDs = enableMogo.dynamicDs();
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
        System.out.println(":: Mogo starting ::           v1.0.0");
        System.out.println(":: gitee         ::           https://gitee.com/lyilan8080/mogo");
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
            LOGGER.info(MogoConstant.LOG_PRE + "mogo [autoFill] switch is enable");
        } else {
            LOGGER.info(MogoConstant.LOG_PRE + "mogo [autoFill] switch is unEnable");
        }
        if (MogoEnableCache.dynamicDs) {
            LOGGER.info(MogoConstant.LOG_PRE + "mogo [dynamicDs] switch is enable");
        } else {
            LOGGER.info(MogoConstant.LOG_PRE + "mogo [dynamicDs] switch is unEnable");
        }

    }

    @Bean
    @Order(Integer.MIN_VALUE)
    public MogoConfiguration mogoConfiguration() {

        MogoConfiguration.instance().template(MogoConstant.MASTER_DS, mongoTemplate);
        return MogoConfiguration.instance();

    }

}
