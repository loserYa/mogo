package com.loser.mysqlCodeGen.gen.handler;

import com.loser.mysqlCodeGen.gen.inner.RunTimeParams;

import java.util.List;

/**
 * 替换处理器
 *
 * @author loser
 * @date 2023-01-06  12:20
 */
public interface ReplaceHandler {

    /**
     * 匹配关键字后替换的代码
     *
     * @return 替换后的代码
     */
    List<String> work();

    /**
     * 匹配关键字
     *
     * @param keyword 需要匹配的关键字
     * @return 是否匹配成功
     */
    boolean match(String keyword);

    /**
     * 运行时动态的往 代码中添加新的变量
     *
     * @param config 运行时的变量集合
     */
    default void putData(RunTimeParams config) {
    }

}
