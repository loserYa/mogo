package com.loser.core.entity;

import com.loser.core.constant.ECompare;
import com.loser.core.constant.EConditionType;
import com.loser.core.wrapper.ConditionWrapper;
import lombok.Data;

import java.util.List;

/**
 * 条件
 *
 * @author loser
 * @date 2023-02-04  17:07
 */
@Data
public class Condition {

    public Condition() {
    }

    public Condition(ECompare type, String col, List<Object> args) {
        this.type = type;
        this.col = col;
        this.args = args;
    }

    /**
     * 嵌套条件比较雷系
     */
    private EConditionType conditionType = EConditionType.AND;

    /**
     * 比较类型
     */
    private ECompare type;

    /**
     * 集合对应的列
     */
    private String col;

    /**
     * 比较值
     */
    private List<Object> args;

    /**
     * 条件包装类
     */
    private ConditionWrapper conditionWrapper;

}
