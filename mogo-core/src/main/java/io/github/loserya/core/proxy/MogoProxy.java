package io.github.loserya.core.proxy;

import java.lang.reflect.InvocationHandler;

public abstract class MogoProxy implements InvocationHandler {

    public MogoProxy(Object target) {
        this.target = target;
    }

    private Object target;

    public Object getTarget() {
        return target;
    }

    public void setTarget(Object target) {
        this.target = target;
    }
}
