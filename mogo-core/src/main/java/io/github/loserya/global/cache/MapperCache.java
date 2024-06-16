package io.github.loserya.global.cache;

import java.util.HashSet;
import java.util.Set;

/**
 * 标记需要注册到容器中的 mapper 对应的集合实体
 *
 * @author loser
 * @since 1.1.6
 */
public class MapperCache {

    public static final Set<Class<?>> MAPPER = new HashSet<>();

}
