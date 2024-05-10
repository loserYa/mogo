package com.loser.config;

import com.loser.function.interceptor.Interceptor;
import org.springframework.stereotype.Component;

@Component
public class MyInterceptor implements Interceptor {

    @Override
    public Object[] build(Object... args) {
        System.out.println("MyInterceptor build invoke " + args.length);
        return Interceptor.super.build(args);
    }
}