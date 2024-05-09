package com.loser.module.loser;

import com.loser.module.logic.CollectionLogic;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

/**
 * 测试数据
 *
 * @author loser
 * @date 2023-02-05  13:56
 */
@Data
@Document("loser_test")
public class Loser {

    @Id
    private Long id;

    private String loginName;

    private String passWord;

    @Transient
    private Integer age;

    private Long createTime;

    private Long updateTime;

    @CollectionLogic
    @Field("logic")
    private String logicDel;

}
