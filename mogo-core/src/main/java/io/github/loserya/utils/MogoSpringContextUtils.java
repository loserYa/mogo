package io.github.loserya.utils;


import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * mogo spring 上下文工具
 *
 * @author loser
 * @since 1.1.6
 */
public class MogoSpringContextUtils implements ApplicationContextAware {

    private static ApplicationContext springContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        springContext = applicationContext;
    }

    /**
     * applicationContext
     */
    public static ApplicationContext getApplicationContext() {
        return springContext;
    }

    /**
     * 通过name获取 Bean. 需要再springboot启动的时候设置applicationContext
     */
    public static Object getBean(String name) {
        return getApplicationContext().getBean(name);
    }

    /**
     * 通过class获取Bean
     */
    public static <T> T getBean(Class<T> clazz) {
        return getApplicationContext().getBean(clazz);
    }

    /**
     * 通过name,以及Clazz返回指定的Bean
     */
    public static <T> T getBean(String name, Class<T> clazz) {
        return getApplicationContext().getBean(name, clazz);
    }

    public static <T> List<T> getBeans(Class<T> clazz) {
        return getBeans(getApplicationContext(), clazz);
    }

    public static <T> List<T> getBeans(ApplicationContext applicationContext, Class<T> clazz) {
        return Arrays.stream(applicationContext.getBeanNamesForType(clazz))
                .map(item -> (T) applicationContext.getBean(item)).collect(Collectors.toList());
    }

}
