package io.github.loserya.global.cache;

import io.github.loserya.module.idgen.hardcode.GEN;
import io.github.loserya.module.idgen.strategy.IdGenStrategy;

import java.util.HashMap;
import java.util.Map;

/**
 * id 生成策略器 缓存
 *
 * @author loser
 * @date 2024/5/12
 */
public class IdGenStrategyCache {

    public static Map<GEN, IdGenStrategy> MAP = new HashMap<>();

}
