package com.loser.common.entity;

import io.swagger.annotations.ApiModelProperty;

public class CommonPage {

    @ApiModelProperty(value = "页码", required = true, example = "1")
    private Integer pageNo = 1;

    @ApiModelProperty(value = "页行数", required = true, example = "20")
    private Integer pageSize = 20;

    public Integer getPageNo() {
        return pageNo;
    }

    public void setPageNo(Integer pageNo) {
        this.pageNo = pageNo;
    }

    public Integer getPageSize() {
        return pageSize;
    }

    public void setPageSize(Integer pageSize) {
        this.pageSize = pageSize;
    }
}
