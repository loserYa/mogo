package com.loser.config;

import com.loser.core.anno.EnableMogo;
import com.loser.global.cache.MogoCache;
import com.loser.hardcode.constant.MogoConstant;
import com.loser.properties.MogoDataSourceProperties;
import com.loser.properties.MogoLogicProperties;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.core.annotation.Order;
import org.springframework.core.env.Environment;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.util.CollectionUtils;

import java.util.Map;

/**
 * 基础必要配置
 *
 * @author loser
 * @date 2024/5/10
 */
@EnableConfigurationProperties({MogoLogicProperties.class, MogoDataSourceProperties.class})
public class MogoBaseConfiguration {


    private static final Log LOGGER = LogFactory.getLog(MogoConfiguration.class);
    private final MongoTemplate mongoTemplate;

    public MogoBaseConfiguration(
            ApplicationContext applicationContext,
            MongoTemplate mongoTemplate,
            Environment environment,
            MogoDataSourceProperties mogoDataSourceProperties,
            MogoLogicProperties mogoLogicProperties
    ) {
        Map<String, Object> beans = applicationContext.getBeansWithAnnotation(EnableMogo.class);
        if (!CollectionUtils.isEmpty(beans)) {
            MogoConfiguration.instance().enableMogo();
        }
        logBaseInfo();

        this.mongoTemplate = mongoTemplate;
        if (MogoCache.open) {
            new MogoAutoConfiguration(environment, mogoLogicProperties, mogoDataSourceProperties);
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
        System.out.println(":: Mogo starIng ::            v1.0.0");
        System.out.println(":: gitee        ::            https://gitee.com/lyilan8080/mogo-parent");
        System.out.println();
        if (MogoCache.open) {
            LOGGER.info(MogoConstant.LOG_PRE + "mogo global switch is enable");
        } else {
            LOGGER.info(MogoConstant.LOG_PRE + "mogo global switch is unEnable");
        }

    }

    @Bean
    @Order(Integer.MIN_VALUE)
    public MogoConfiguration mogoConfiguration() {

        MogoConfiguration.instance().template(MogoConstant.MASTER_DS, mongoTemplate);
        return MogoConfiguration.instance();

    }

}
