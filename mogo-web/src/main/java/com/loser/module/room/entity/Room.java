package com.loser.module.room.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;

/**
 * 房间 实体
 *
 * @author loser
 * @date 2023-08-05 20:26:45
 */
@Data
public class Room {

    /**
     * id
     */
    @Id
    private String id;

    /**
     * 名称
     */
    private String name;

    /**
     * 类型
     */
    private Integer type;

    /**
     * 创建时间
     */
    private Long creatTime;

}
