package com.loser.module.room.vo.req;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;


/**
 * 房间保存请求参数
 *
 * @author loser
 * @date 2023-08-05 20:26:45
 */
@Data
@ApiModel("房间保存请求参数")
public class RoomSaveReq {

    @ApiModelProperty(value = "id")
    private String id;

    @ApiModelProperty(value = "名称")
    private String name;

    @ApiModelProperty(value = "类型")
    private Integer type;

    @ApiModelProperty(value = "创建时间")
    private Long creatTime;

}
