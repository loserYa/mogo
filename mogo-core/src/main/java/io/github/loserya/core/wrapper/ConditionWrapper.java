/**
 * ConditionWrapper.java 代码解读
 * 这段代码是一个Java类，名为ConditionWrapper，它属于io.github.loserya.core.wrapper包。这个类的主要作用是作为数据库查询时的条件包装器，用于封装查询条件、排序条件、分页信息等。下面是对代码的详细解释：
 * <p>
 * 类定义：public class ConditionWrapper 定义了一个公共类ConditionWrapper。
 * <p>
 * 构造函数：
 * <p>
 * public ConditionWrapper(List<SelectField> fields, List<Condition> conditions, List<SortCondition> sortConditions, Long skip, Integer limit)：这是一个带参数的构造函数，用于初始化类的属性。它接受五个参数：字段列表（fields）、条件列表（conditions）、排序条件列表（sortConditions）、跳过的记录数（skip）和限制的记录数（limit）。
 * public ConditionWrapper()：这是一个无参构造函数，用于创建一个默认的ConditionWrapper实例。
 * 属性：
 * <p>
 * private List<SelectField> fields = Collections.emptyList();：存储查询的字段列表，默认为空列表。
 * private List<Condition> conditions = Collections.emptyList();：存储查询条件，默认为空列表。
 * private List<SortCondition> sortConditions = Collections.emptyList();：存储排序条件，默认为空列表。
 * private Long skip;：用于分页的跳过记录数。
 * private Integer limit;：用于分页的限制记录数。
 * 方法：
 * <p>
 * getFields(), setFields(List<SelectField> fields)：获取和设置字段列表的方法。
 * getConditions(), setConditions(List<Condition> conditions)：获取和设置条件列表的方法。
 * getSortConditions(), setSortConditions(List<SortCondition> sortConditions)：获取和设置排序条件列表的方法。
 * getSkip(), setSkip(Long skip)：获取和设置跳过记录数的方法。
 * getLimit(), setLimit(Integer limit)：获取和设置限制记录数的方法。
 * 这个类是一个典型的数据封装类，用于在数据库操作中管理和传递查询相关的数据。通过这种方式，可以方便地构建和管理复杂的查询条件，提高代码的可读性和可维护性。
 */
package io.github.loserya.core.wrapper;

import io.github.loserya.core.entity.Condition;
import io.github.loserya.core.entity.SelectField;
import io.github.loserya.core.entity.SortCondition;
import io.github.loserya.core.entity.UpdateField;

import java.util.Collections;
import java.util.List;

/**
 * 条件包装类
 *
 * @author loser
 * @since 1.0.0
 */
public class ConditionWrapper {

    public ConditionWrapper(List<SelectField> fields, List<Condition> conditions, List<SortCondition> sortConditions, List<UpdateField> updateFields, Long skip, Integer limit) {
        this.fields = fields;
        this.conditions = conditions;
        this.sortConditions = sortConditions;
        this.updateFields = updateFields;
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
     * 需要更新的值
     */
    private List<UpdateField> updateFields = Collections.emptyList();

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

    public void setUpdateFields(List<UpdateField> updateFields) {
        this.updateFields = updateFields;
    }

    public List<UpdateField> getUpdateFields() {
        return updateFields;
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
