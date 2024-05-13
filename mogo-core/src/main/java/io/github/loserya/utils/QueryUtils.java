package io.github.loserya.utils;

import io.github.loserya.core.wrapper.LambdaQueryWrapper;
import io.github.loserya.core.wrapper.Wrappers;
import io.github.loserya.hardcode.constant.MogoConstant;

import java.io.Serializable;
import java.util.Collection;

@SuppressWarnings("all")
public class QueryUtils {

    private QueryUtils() {
    }

    public static <T> LambdaQueryWrapper<T> buildEq(Object obj, Class<T> clazz) {
        return new Wrappers().<T>lambdaQuery().eq(MogoConstant.ID, ClassUtil.getId(obj));
    }

    public static <T> LambdaQueryWrapper<T> buildEq(Serializable id, Class<T> clazz) {
        return new Wrappers().<T>lambdaQuery().eq(MogoConstant.ID, id);
    }

    public static <T> LambdaQueryWrapper<T> buildIn(Collection ids, Class<T> clazz) {
        return new Wrappers().<T>lambdaQuery().in(MogoConstant.ID, ids);
    }

}
