package com.loser.mysqlCodeGen.gen.inner;

import lombok.Data;

/**
 * 代码路径配置
 *
 * @author loser
 * @date 2023/2/6 0:16
 */
@Data
public class CodePathConfig {

    /**
     * 生成的文件目录 以当前父相关根路径为开始位置
     */
    private String path;

    /**
     * 生成的文件名称
     */
    private String fileName;

    /**
     * 生成的文件后缀
     */
    private String suffix;

    /**
     * 模板文件
     */
    private String file;

}
