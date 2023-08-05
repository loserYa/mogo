package com.loser.gen;

import com.loser.gen.biz.MogoMeta;
import org.springframework.data.annotation.Id;

/**
 * todo
 *
 * @author loser
 * @date 2023-02-05  15:14
 */
@MogoMeta("房间")
public class Room {

    @Id
    @MogoMeta("id")
    private String id;

    @MogoMeta("名称")
    private String name;

    @MogoMeta("类型")
    private Integer type;

    @MogoMeta("创建时间")
    private Long creatTime;

}
