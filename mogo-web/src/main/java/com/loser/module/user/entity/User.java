package com.loser.module.user.entity;

import lombok.Data;
import org.springframework.data.annotation.Id;

/**
 * 测试数据
 *
 * @author loser
 * @date 2023-02-05  13:56
 */
@Data
public class User {

    @Id
    private Long id;

    private String loginName;

    private String passWord;

    private Integer age;

    private Long createTime;

    private Long updateTime;

    private String logicDel;

}
