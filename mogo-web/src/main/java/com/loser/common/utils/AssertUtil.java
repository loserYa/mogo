package com.loser.common.utils;

import cn.hutool.core.collection.CollectionUtil;
import org.springframework.util.StringUtils;

import java.util.Objects;

public class AssertUtil {

    public static void notNull(Object obj, String msg) {
        if (Objects.isNull(obj)) {
            throw new RuntimeException(msg);
        }
    }

    public static void notTrue(boolean isNotTrue, String msg) {
        if (!isNotTrue) {
            throw new RuntimeException(msg);
        }
    }

    public static void isTrue(boolean isTrue, String msg) {
        if (isTrue) {
            throw new RuntimeException(msg);
        }
    }

    public static void notEmpty(String str, String msg) {
        if (StringUtils.isEmpty(str)) {
            throw new RuntimeException(msg);
        }
    }

    public static void notEmpty(Iterable list, String msg) {
        if (CollectionUtil.isEmpty(list)) {
            throw new RuntimeException(msg);
        }
    }

    public static void lteZero(int obj, String msg) {
        if (obj <= 0) {
            throw new RuntimeException(msg);
        }
    }

}
