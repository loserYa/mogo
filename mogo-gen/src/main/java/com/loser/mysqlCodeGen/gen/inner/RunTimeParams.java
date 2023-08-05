package com.loser.mysqlCodeGen.gen.inner;

import cn.hutool.core.lang.Pair;
import cn.hutool.core.util.StrUtil;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * 运行时 存储的变量
 *
 * @author loser
 * @date 2023/2/6 0:14
 */
@Data
public class RunTimeParams {

    /**
     * 存储的变量
     */
    private List<Pair<String, String>> pairs;

    public RunTimeParams() {
        this.pairs = new ArrayList<>();
    }

    /**
     * 添加新的变量 非空默认添加小驼峰
     *
     * @param pair 变量值
     * @return 当前对象
     */
    public RunTimeParams append(Pair<String, String> pair) {
        this.pairs.add(pair);
        String key = pair.getKey();
        String value = pair.getValue();
        if (!StrUtil.isEmpty(value)) {
            Pair<String, String> temp = new Pair<>("_" + key, value.substring(0, 1).toLowerCase() + value.substring(1));
            this.pairs.add(temp);
        }
        return this;
    }

    /**
     * 添加新的变量 非空默认添加小驼峰
     *
     * @param key   占位符
     * @param value 替换值
     * @return 当前对象
     */
    public RunTimeParams append(String key, String value) {

        String result = getValueByKey(key, true);
        if (!"".equals(result)) {
            delKey(key);
            delKey("_" + key);
        }
        Pair<String, String> pair = new Pair<>(key, value);
        return append(pair);

    }

    /**
     * 删除运行时候的变量
     *
     * @param key 占位符
     */
    private void delKey(String key) {
        pairs.removeIf(next -> next.getKey().equals(key));
    }

    /**
     * 通过key获取value
     *
     * @param key            key
     * @param firstLowercase 首字母是否需要小写
     * @return key对应的value
     */
    public String getValueByKey(String key, boolean firstLowercase) {
        for (Pair<String, String> pair : pairs) {
            if (pair.getKey().equals(key)) {
                String result = pair.getValue();
                if (firstLowercase) {
                    result = result.substring(0, 1).toLowerCase() + result.substring(1);
                }
                return result;
            }
        }
        return "";
    }

}