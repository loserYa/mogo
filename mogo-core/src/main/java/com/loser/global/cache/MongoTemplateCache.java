package com.loser.global.cache;

import com.loser.hardcode.constant.MogoConstant;
import com.loser.utils.StringUtils;
import org.springframework.data.mongodb.core.MongoTemplate;

import java.util.HashMap;
import java.util.Map;

/**
 * 缓存多个数据源信息
 *
 * @author loser
 * @date 2024/5/9
 */
public class MongoTemplateCache {

    public static final Map<String, MongoTemplate> CACHE = new HashMap<>();

    private static final ThreadLocal<String> dataSource = new InheritableThreadLocal<>();

    public static void setDataSource(String ds) {
        dataSource.set(ds);
    }

    public static String getDataSource() {

        String ds = dataSource.get();
        if (StringUtils.isBlank(ds)) {
            ds = MogoConstant.MASTER_DS;
        }
        return ds;

    }

    public static void clear() {
        dataSource.remove();
    }

}
