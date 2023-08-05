package com.loser.core.wrapper;

import com.loser.core.entity.Condition;
import com.loser.core.entity.SelectField;
import com.loser.core.entity.SortCondition;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Collections;
import java.util.List;

/**
 * 条件包装类
 *
 * @author loser
 * @date 2023-02-04  17:07
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ConditionWrapper {

    /**
     * 主动查询的集合字段
     */
    private List<SelectField> fields = Collections.emptyList();

    /**
     * 条件构建器参数集合
     */
    private List<Condition> conditions = Collections.emptyList();

    /**
     * 条件构建器排序集合
     */
    private List<SortCondition> sortConditions = Collections.emptyList();

    /**
     * 分页 skip
     */
    private Long skip;

    /**
     * 分页 limit
     */
    private Integer limit;

}
