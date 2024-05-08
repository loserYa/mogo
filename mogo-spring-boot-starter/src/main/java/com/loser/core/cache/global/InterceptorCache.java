package com.loser.core.cache.global;


import com.loser.core.interceptor.Interceptor;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;


public class InterceptorCache {

    public static List<Interceptor> interceptors = new ArrayList<>();

    public static void sorted() {
        interceptors = interceptors.stream().sorted(Comparator.comparing(Interceptor::order)).collect(Collectors.toList());
    }

}
