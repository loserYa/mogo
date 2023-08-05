package com.loser.module.user.vo.resp;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 测试数据 详情数据
 *
 * @author loser
 * @date 2023-02-05  14:05
 */
@Data
@ApiModel("测试数据 详情数据")
public class UserInfoResp {

    @ApiModelProperty(value = "id")
    private Long id;

    @ApiModelProperty(value = "登录名称")
    private String loginName;

    @ApiModelProperty(value = "密码")
    private String passWord;

    @ApiModelProperty(value = "年龄")
    private Integer age;

    @ApiModelProperty(value = "创建时间")
    private Long createTime;

    @ApiModelProperty(value = "修改时间")
    private Long updateTime;

}
