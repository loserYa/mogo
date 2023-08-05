package com.loser.mysqlCodeGen.gen.utils;

import lombok.Data;

import java.io.InputStream;
import java.util.Properties;

/**
 * Properties 文件读取器
 *
 * @author loser
 * @date 2023/2/6 0:18
 */
@Data
public class PropReader {

    /**
     * 配置文件
     */
    private Properties properties;

    /**
     * 读取配置文件
     *
     * @param file 文件路径
     */
    public PropReader(String file) {

        InputStream in;
        try {
            properties = new Properties();
            in = PropReader.class.getClassLoader().getResourceAsStream(file);
            properties.load(in);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * 获取配置中的值
     *
     * @param key key
     * @return 值
     */
    public String getValue(String key) {
        return properties.getProperty(key);
    }

}
