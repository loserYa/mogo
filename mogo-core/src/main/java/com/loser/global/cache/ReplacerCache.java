package com.loser.global.cache;


import com.loser.function.replacer.Replacer;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * executor 替换器缓存
 *
 * @author loser
 * @date 2024/4/30
 */
public class ReplacerCache {

    public static List<Replacer> replacers = new ArrayList<>();

    public static void sorted() {
        replacers = replacers.stream().sorted(Comparator.comparing(Replacer::order)).collect(Collectors.toList());
    }

}
