package com.loser.mysqlCodeGen.gen.utils;

import java.lang.reflect.Field;


/**
 * 配置文件工具
 *
 * @author loser
 * @date 2023/2/6 0:20
 */
public class PropUtil {

    /**
     * 获取一个配置文件reader
     *
     * @param file 文件
     * @return 包含了配置文件信息的reader
     */
    private static PropReader init(String file) {
        return new PropReader(file);
    }

    /**
     * 将配置文件读取成对象（只设置string的只）
     *
     * @param targetClass 对象类型
     * @param file        配置文件
     * @param pre         统一前端
     * @param <T>         对象类型
     * @return 对象
     */
    public static <T> T loadStrValue(Class<T> targetClass, String file, String pre) {

        T result = null;
        try {
            PropReader reader = init(file);
            result = targetClass.newInstance();
            Field[] fields = targetClass.getDeclaredFields();
            for (Field field : fields) {
                field.setAccessible(true);
                String value = reader.getValue(pre + "." + field.getName());
                if (field.getType() == String.class) {
                    field.set(result, value);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

}
