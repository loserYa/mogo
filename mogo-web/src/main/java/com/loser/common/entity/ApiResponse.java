package com.loser.common.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.annotations.ApiModelProperty;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 业务正常时的响应体
 **/
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class ApiResponse<T> implements Serializable {

    private static final long serialVersionUID = 7581339796446717946L;

    /**
     * 业务状态码
     */
    @ApiModelProperty(value = "业务状态码", required = true, example = "OK")
    private String code;
    /**
     * 业务消息
     */
    @ApiModelProperty(value = "业务消息", required = true, example = "SUCCESS")
    private String message;
    /**
     * 业务数据
     */
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private T data;


    /**
     * 自定义成功的返回消息
     */
    public static <T> ApiResponse<T> success(String msg) {
        return new ApiResponse<>(
                CommonBizStatus.OK.getCode(),
                msg,
                null
        );
    }

    /**
     * 正常响应体，不带data
     */
    public static <T> ApiResponse<T> ok() {
        return new ApiResponse<>(
                CommonBizStatus.OK.getCode(),
                CommonBizStatus.OK.getMsg(),
                null
        );
    }

    /**
     * 正常响应体，带data
     */
    public static <T> ApiResponse<T> ok(T data) {
        return new ApiResponse<>(
                CommonBizStatus.OK.getCode(),
                CommonBizStatus.OK.getMsg(),
                data
        );
    }

    /**
     * 异常响应体，不带data
     */
    public static <T> ApiResponse<T> err(IBizStatus status) {
        return new ApiResponse<>(
                status.getCode(),
                status.getMsg(),
                null
        );
    }

    /**
     * 异常响应体，带data
     */
    public static <T> ApiResponse<T> err(IBizStatus status, T data) {
        return new ApiResponse<>(
                status.getCode(),
                status.getMsg(),
                data
        );
    }

    /**
     * 异常响应体，自定义msg，不data
     */
    public static <T> ApiResponse<T> err(IBizStatus status, String msg) {
        return new ApiResponse<>(
                status.getCode(),
                msg,
                null
        );
    }

    /**
     * 异常响应体，自定义msg，带data
     */
    public static <T> ApiResponse<T> err(IBizStatus status, String msg, T data) {
        return new ApiResponse<>(
                status.getCode(),
                msg,
                data
        );
    }
}