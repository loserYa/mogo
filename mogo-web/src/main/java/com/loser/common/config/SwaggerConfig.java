package com.loser.common.config;

import com.github.xiaoymin.knife4j.spring.annotations.EnableKnife4j;
import com.loser.common.anto.ApiConst;
import com.loser.common.anto.ApiVersion;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.Arrays;

@Configuration
@EnableSwagger2
@EnableKnife4j
public class SwaggerConfig {

    @Bean
    public Docket loser() {
        return buildDocketWithGroupName(ApiConst.LOSER);
    }

    /**
     * 文档信息
     *
     * @param groupName 分组名
     * @return 文档信息实例
     */
    private ApiInfo apiInfo(String groupName) {
        return new ApiInfoBuilder()
                .title("API docs")
                .description("#API-".concat(groupName))
                .contact(new Contact(
                        "LOSER",
                        "",
                        ""
                ))
                .license("")
                .licenseUrl("")
                .version(groupName)
                .build();
    }

    private Docket buildDocketWithGroupName(String groupName) {

        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo(groupName))
                .groupName(groupName)
                .select()
                .apis(input -> {
                    if (input.getHandlerMethod().hasMethodAnnotation(ApiVersion.class)) {
                        ApiVersion apiVersion = input.getHandlerMethod().getMethodAnnotation(ApiVersion.class);
                        if (apiVersion.value() != null && apiVersion.value().length != 0) {
                            if (Arrays.asList(apiVersion.value()).contains(groupName)) {
                                return true;
                            }
                        }
                    }
                    ApiVersion clazzApiVersion = input.getHandlerMethod().getBeanType().getAnnotation(ApiVersion.class);
                    if (clazzApiVersion != null) {
                        if (clazzApiVersion.value() != null && clazzApiVersion.value().length != 0) {
                            return Arrays.asList(clazzApiVersion.value()).contains(groupName);
                        }
                    }
                    return false;
                })
                .paths(PathSelectors.any())
                .build();

    }

}
