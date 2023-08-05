package com.loser.gen;

import com.loser.gen.biz.MogoMeta;
import org.springframework.data.annotation.Id;

/**
 * todo
 *
 * @author loser
 * @date 2023-02-05  15:14
 */
@MogoMeta("失败者")
public class Loser {

    @Id
    @MogoMeta("id")
    private Long id;

    @MogoMeta("名称")
    private String name;

    @MogoMeta("年龄")
    private Integer age;

    @MogoMeta("创建时间")
    private Long creatTime;

}
