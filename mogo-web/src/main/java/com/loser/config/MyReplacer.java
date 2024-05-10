package com.loser.config;

import com.loser.function.replacer.Replacer;
import com.loser.utils.func.BoolFunction;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

@Component
public class MyReplacer implements Replacer {

    @Override
    public Object invoke(Class<?> clazz, Object proxy, Object target, Method method, Object[] args) throws Throwable {
        System.out.println("MyReplacer invoke " + method.getName());
        return method.invoke(target, args);
    }

    @Override
    public BoolFunction supplier() {
        return (proxy, target, method, args) -> true;
    }
}
