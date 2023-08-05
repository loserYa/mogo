package com.loser.gen.biz;

import com.loser.mysqlCodeGen.gen.MapperConfig;
import com.loser.mysqlCodeGen.gen.inner.CodeMapper;

public class MogoMapperConfig extends MapperConfig {

    private static final String basePath = "/mogo-web/src/main/java/com/loser/module";

    @CodeMapper(
            basePath = basePath,
            path = "/#<module>/entity/",
            file = "#<modelName>",
            suffix = ".java",
            tem = "template/Demo.template"
    )
    private String domain;

    @CodeMapper(
            basePath = basePath,
            path = "/#<module>/service/",
            file = "#<modelName>Service",
            suffix = ".java",
            tem = "template/DemoService.template"
    )
    private String service;

    @CodeMapper(
            basePath = basePath,
            path = "/#<module>/service/impl/",
            file = "#<modelName>ServiceImpl",
            suffix = ".java",
            tem = "template/DemoServiceImpl.template"
    )
    private String serviceImpl;

    @CodeMapper(
            basePath = basePath,
            path = "/#<module>/handler/",
            file = "#<modelName>Handler",
            suffix = ".java",
            tem = "template/DemoHandler.template"
    )
    private String handler;

    @CodeMapper(
            basePath = basePath,
            path = "/#<module>/vo/req/",
            file = "#<modelName>SaveReq",
            suffix = ".java",
            tem = "template/DemoSaveReq.template"
    )
    private String saveReq;

    @CodeMapper(
            basePath = basePath,
            path = "/#<module>/vo/req/",
            file = "#<modelName>UpdateReq",
            suffix = ".java",
            tem = "template/DemoUpdateReq.template"
    )
    private String updateReq;

    @CodeMapper(
            basePath = basePath,
            path = "/#<module>/vo/req/",
            file = "#<modelName>PageReq",
            suffix = ".java",
            tem = "template/DemoPageReq.template"
    )
    private String pageReq;

    @CodeMapper(
            basePath = basePath,
            path = "/#<module>/vo/resp/",
            file = "#<modelName>InfoResp",
            suffix = ".java",
            tem = "template/DemoInfoResp.template"
    )
    private String infoResp;

    @CodeMapper(
            basePath = basePath,
            path = "/#<module>/controller/",
            file = "#<modelName>Controller",
            suffix = ".java",
            tem = "template/DemoController.template"
    )
    private String controller;

}
