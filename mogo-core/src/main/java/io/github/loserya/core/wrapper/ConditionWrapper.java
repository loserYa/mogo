package io.github.loserya.core.wrapper;

import io.github.loserya.core.entity.Condition;
import io.github.loserya.core.entity.SelectField;
import io.github.loserya.core.entity.SortCondition;

import java.util.Collections;
import java.util.List;

/**
 * 条件包装类
 *
 * @author loser
 * @since 1.0.0
 */
public class ConditionWrapper {

    public ConditionWrapper(List<SelectField> fields, List<Condition> conditions, List<SortCondition> sortConditions, Long skip, Integer limit) {
        this.fields = fields;
        this.conditions = conditions;
        this.sortConditions = sortConditions;
        this.skip = skip;
        this.limit = limit;
    }

    public ConditionWrapper() {
    }

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

    public List<SelectField> getFields() {
        return fields;
    }

    public void setFields(List<SelectField> fields) {
        this.fields = fields;
    }

    public List<Condition> getConditions() {
        return conditions;
    }

    public void setConditions(List<Condition> conditions) {
        this.conditions = conditions;
    }

    public List<SortCondition> getSortConditions() {
        return sortConditions;
    }

    public void setSortConditions(List<SortCondition> sortConditions) {
        this.sortConditions = sortConditions;
    }

    public Long getSkip() {
        return skip;
    }

    public void setSkip(Long skip) {
        this.skip = skip;
    }

    public Integer getLimit() {
        return limit;
    }

    public void setLimit(Integer limit) {
        this.limit = limit;
    }
}
