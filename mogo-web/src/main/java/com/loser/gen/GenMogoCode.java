package com.loser.gen;

import cn.hutool.core.date.DateUtil;
import com.loser.gen.biz.ApiReplaceHandler;
import com.loser.gen.biz.BeanReplaceHandler;
import com.loser.gen.biz.MogoMapperConfig;
import com.loser.gen.biz.MyCusConfig;
import com.loser.mysqlCodeGen.gen.Gen;
import com.loser.mysqlCodeGen.gen.handler.ReplaceHandler;
import com.loser.mysqlCodeGen.gen.inner.RunTimeParams;
import com.loser.mysqlCodeGen.gen.utils.PropUtil;

import java.util.Arrays;
import java.util.List;

/**
 * @author loser
 * @date 2023-01-06  14:26
 */
public class GenMogoCode {

    public static void main(String[] args) {
        genCode();
    }

    public static void genCode() {

        try {
            // 01 获取自定义配置
            MyCusConfig cusConfig = PropUtil.loadStrValue(MyCusConfig.class, "genCodeConfig.properties", "customer");
            String module = cusConfig.getModule();
            String author = cusConfig.getAuthor();
            String targetClass = cusConfig.getTargetClass();

            // 02 添加模板中的替换值
            RunTimeParams runTimeParams = new RunTimeParams();
            runTimeParams.append("author", author)
                    .append("genDate", DateUtil.now())
                    .append("module", module)
            ;

            String[] list = targetClass.split(",");
            for (String target : list) {
                Class<?> targetClazz = Class.forName(target);
                List<ReplaceHandler> handlers = Arrays.asList(new ApiReplaceHandler(targetClazz), new BeanReplaceHandler(targetClazz));
                Gen.genFile(handlers, runTimeParams, MogoMapperConfig.class);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}
