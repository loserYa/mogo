package io.github.loserya.core.handler;


import io.github.loserya.core.anno.EnableMogo;
import io.github.loserya.global.cache.MogoEnableCache;
import io.github.loserya.hardcode.constant.MogoConstant;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.annotation.AnnotationAttributes;
import org.springframework.core.type.AnnotationMetadata;

import java.util.Objects;

public class MogoEnableHandler implements ImportBeanDefinitionRegistrar {

    private static final Log LOGGER = LogFactory.getLog(MogoEnableHandler.class);

    @Override
    public void registerBeanDefinitions(AnnotationMetadata metadata, BeanDefinitionRegistry registry) {

        AnnotationAttributes enable = AnnotationAttributes.fromMap(metadata.getAnnotationAttributes(EnableMogo.class.getName()));
        if (Objects.nonNull(enable)) {
            MogoEnableCache.base = Boolean.TRUE.equals(enable.getBoolean("base"));
            MogoEnableCache.logic = Boolean.TRUE.equals(enable.getBoolean("logic"));
            MogoEnableCache.autoFill = Boolean.TRUE.equals(enable.getBoolean("autoFill"));
            MogoEnableCache.dynamicDs = Boolean.TRUE.equals(enable.getBoolean("dynamicDs"));
            MogoEnableCache.banFullTable = Boolean.TRUE.equals(enable.getBoolean("banFullTable"));
            MogoEnableCache.transaction = Boolean.TRUE.equals(enable.getBoolean("transaction"));
            MogoEnableCache.debugLog = Boolean.TRUE.equals(enable.getBoolean("debugLog"));
        }
        logBaseInfo();

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
        System.out.println(":: Mogo starting ::           v1.1.8");
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
