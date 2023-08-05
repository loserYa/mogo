package com.loser.mysqlCodeGen.gen.inner;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


/**
 * 代码模板映射
 *
 * @author loser
 * @date 2023/2/6 0:16
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface CodeMapper {

    /**
     * 基础文件路径
     *
     * @return
     */
    String basePath();

    /**
     * 代码生成路径
     */
    String path();

    /**
     * 代码生成文件
     */
    String file();

    /**
     * 文件后缀
     */
    String suffix();

    /**
     * 模板文件
     */
    String tem();

}
