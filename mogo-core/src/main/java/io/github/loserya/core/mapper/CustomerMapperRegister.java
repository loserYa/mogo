package io.github.loserya.core.mapper;

import java.util.Set;

/**
 * 用户自定义 mapper 对象交给 spring 管理
 *
 * @author loser
 * @since 1.1.6
 */
public interface CustomerMapperRegister {

    Set<Class<?>> register();

}
