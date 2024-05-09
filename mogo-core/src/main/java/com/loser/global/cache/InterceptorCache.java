package com.loser.global.cache;


import com.loser.function.interceptor.Interceptor;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 拦截器缓存
 *
 * @author loser
 * @date 2024/5/9
 */
public class InterceptorCache {

    public static List<Interceptor> interceptors = new ArrayList<>();

    public static void sorted() {
        interceptors = interceptors.stream().sorted(Comparator.comparing(Interceptor::order)).collect(Collectors.toList());
    }

}
