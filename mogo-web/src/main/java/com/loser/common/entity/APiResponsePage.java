package com.loser.common.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.loser.common.utils.ParamsUtil;
import com.loser.core.entity.Page;
import io.swagger.annotations.ApiModelProperty;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

/**
 * 对Page<E>结果进行包装
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class APiResponsePage<B, E> implements Serializable {

    private static final long serialVersionUID = -9155809568358479424L;

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
     * 页码
     */
    @ApiModelProperty(value = "页码", required = true)
    private int current;

    /**
     * 页行数
     */
    @ApiModelProperty(value = "页行数", required = true)
    private int pageSize;

    /**
     * 总数
     */
    @ApiModelProperty(value = "总行数", required = true)
    private Long total;
    /**
     * 列表数据
     */
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<B> list;

    /**
     * 附加数据
     */
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private E extData;


    /**
     * 正常响应体，不带 list、data
     *
     * @param <B>
     * @return
     */
    public static <B, D> APiResponsePage<B, D> ok() {
        return new APiResponsePage<>(
                CommonBizStatus.OK.getCode(),
                CommonBizStatus.OK.getMsg(),
                1,
                0,
                0L,
                null,
                null
        );
    }

    /**
     * 正常响应体 不带 data
     */
    public static <B, D> APiResponsePage<B, D> ok(Page<B> page) {
        return new APiResponsePage<>(
                CommonBizStatus.OK.getCode(),
                CommonBizStatus.OK.getMsg(),
                page.getPageNum(),
                page.getPageSize(),
                page.getTotal(),
                page.getRecords(),
                null
        );
    }

    /**
     * 正常响应体
     */
    public static <B, D> APiResponsePage<B, D> ok(Page<B> page, D data) {
        return new APiResponsePage<>(
                CommonBizStatus.OK.getCode(),
                CommonBizStatus.OK.getMsg(),
                page.getPageNum(),
                page.getPageSize(),
                page.getTotal(),
                page.getRecords(),
                data
        );
    }

    /**
     * 正常响应体
     */
    public static <B, D> APiResponsePage<B, D> ok(Page<?> page, D data, Class<D> clazz) {
        return new APiResponsePage(
                CommonBizStatus.OK.getCode(),
                CommonBizStatus.OK.getMsg(),
                page.getPageNum(),
                page.getPageSize(),
                page.getTotal(),
                ParamsUtil.mapList(page.getRecords(), clazz),
                data
        );
    }


    /**
     * 正常响应体
     */
    public static <B, D> APiResponsePage<B, D> okPage(Page<?> page, Class<B> clazz) {
        return new APiResponsePage(
                CommonBizStatus.OK.getCode(),
                CommonBizStatus.OK.getMsg(),
                page.getPageNum(),
                page.getPageSize(),
                page.getTotal(),
                ParamsUtil.mapList(page.getRecords(), clazz),
                null
        );
    }

    /**
     * 正常响应体，带 list
     */
    public static <B, D> APiResponsePage<B, D> ok(int current, int pageSize, Long total, List<B> list) {
        return new APiResponsePage<>(
                CommonBizStatus.OK.getCode(),
                CommonBizStatus.OK.getMsg(),
                current,
                pageSize,
                total,
                list,
                null
        );
    }

    /**
     * 正常响应体，带 list、data
     */
    public static <B, D> APiResponsePage<B, D> ok(int current, int pageSize, Long total, List<B> list, D data) {
        return new APiResponsePage<>(
                CommonBizStatus.OK.getCode(),
                CommonBizStatus.OK.getMsg(),
                current,
                pageSize,
                total,
                list,
                data
        );
    }

    /**
     * 异常响应体，不带data、data
     */
    public static <B, D> APiResponsePage<B, D> err(IBizStatus status) {
        return new APiResponsePage<>(
                status.getCode(),
                status.getMsg(),
                1,
                0,
                0L,
                null,
                null
        );
    }

    /**
     * 异常响应体，自定义msg，不list、data
     */
    public static <B, D> APiResponsePage<B, D> err(IBizStatus status, String msg) {
        return new APiResponsePage<>(
                status.getCode(),
                msg,
                1,
                0,
                0L,
                null,
                null
        );
    }

    /**
     * 异常响应体，带list
     */
    public static <B, D> APiResponsePage<B, D> err(IBizStatus status, List<B> list) {
        return new APiResponsePage<>(
                status.getCode(),
                status.getMsg(),
                1,
                0,
                0L,
                list,
                null
        );
    }

    /**
     * 异常响应体，自定义msg，带list
     */
    public static <B, D> APiResponsePage<B, D> err(IBizStatus status, String msg, List<B> list) {
        return new APiResponsePage<>(
                status.getCode(),
                msg,
                1,
                0,
                0L,
                list,
                null
        );
    }

    /**
     * 异常响应体，带list、data
     */
    public static <B, D> APiResponsePage<B, D> err(IBizStatus status, List<B> list, D data) {
        return new APiResponsePage<>(
                status.getCode(),
                status.getMsg(),
                1,
                0,
                0L,
                list,
                data
        );
    }

    /**
     * 异常响应体，自定义msg，带list、data
     */
    public static <B, D> APiResponsePage<B, D> err(IBizStatus status, String msg, List<B> list, D data) {
        return new APiResponsePage<>(
                status.getCode(),
                msg,
                1,
                0,
                0L,
                list,
                data
        );
    }
}
