package com.loser.module.user.vo.req;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 测试数据 更新参数
 *
 * @author loser
 * @date 2023-02-05  14:05
 */
@Data
@ApiModel("测试数据 更新参数")
public class UserUpdateReq {

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
