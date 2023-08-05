package com.loser.module.user.vo.req;

import com.loser.common.entity.CommonPage;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;


/**
 * 测试数据 分页参数
 *
 * @author loser
 * @date 2023-02-05  14:05
 */
@EqualsAndHashCode(callSuper = true)
@Data
@ApiModel("测试数据 分页参数")
public class UserPageReq extends CommonPage {

    @ApiModelProperty(value = "id")
    private Long id;

    @ApiModelProperty(value = "登录名称")
    private String loginName;

    @ApiModelProperty(value = "密码")
    private String passWord;

    @ApiModelProperty(value = "年龄")
    private Integer age;

    @ApiModelProperty(value = "开始时间")
    private Long startTime;

    @ApiModelProperty(value = "结束时间")
    private Long endTime;

}
