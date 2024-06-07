/**
 * LambdaQueryWrapper.java 代码解读
 * 这段代码是一个Java类，名为LambdaQueryWrapper，它实现了多个接口，用于构建和执行数据库查询。这个类是使用Java 8的lambda表达式和函数式编程特性来简化数据库查询构建过程的。下面是对这段代码的详细解释：
 * <p>
 * 类定义：
 * <p>
 * LambdaQueryWrapper<T>是一个泛型类，其中T代表实体类的类型。
 * 它实现了多个接口，包括Compare, ColumnCompare, Func, ColumnFunc, PageFunc, Nested等，这些接口定义了各种数据库操作的方法。
 * 成员变量：
 * <p>
 * fields: 存储选择的字段。
 * conditions: 存储查询条件。
 * sortConditions: 存储排序条件。
 * skip: 用于分页查询的跳过的记录数。
 * limit: 用于分页查询的限制记录数。
 * 方法实现：
 * <p>
 * appendCondition: 添加另一个LambdaQueryWrapper的条件。
 * getConditions: 获取当前的查询条件。
 * conditionsSize: 获取查询条件的数量。
 * getCondition: 获取当前的条件包装器。
 * getFieldMeta: 将lambda表达式转换为字段名。
 * 各种比较方法（如eq, ne, le, lt, ge, gt, between, in, notIn）: 这些方法用于添加不同的查询条件。
 * or 和 and: 用于组合查询条件。 - orderByAsc 和 orderByDesc: 用于添加排序条件。
 * skip 和 limit: 用于设置分页参数。
 * select: 用于选择特定的字段。
 * 功能：
 * <p>
 * 这个类提供了一种流畅的API来构建数据库查询，使得代码更加简洁和易于理解。
 * 它利用了Java 8的lambda表达式和函数式编程特性，提高了代码的可读性和可维护性。
 * 通过实现不同的接口，它提供了丰富的方法来构建复杂的查询，包括条件过滤、排序、分页等。
 * 使用场景：
 * <p>
 * 这个类适用于需要构建复杂数据库查询的场景，特别是在使用JPA或类似ORM框架时。 - 它使得构建查询更加直观和灵活，减少了样板代码。
 * 总的来说，LambdaQueryWrapper类是一个功能强大的工具，用于简化和优化数据库查询构建过程。通过提供一系列流畅的API，它使得编写数据库查询变得更加容易和高效。
 */
package io.github.loserya.core.wrapper;

import io.github.loserya.core.entity.Condition;
import io.github.loserya.core.entity.SelectField;
import io.github.loserya.core.entity.SortCondition;
import io.github.loserya.core.entity.UpdateField;
import io.github.loserya.core.sdk.base.ColumnCompare;
import io.github.loserya.core.sdk.base.ColumnFunc;
import io.github.loserya.core.sdk.base.ColumnUpdate;
import io.github.loserya.core.sdk.base.Compare;
import io.github.loserya.core.sdk.base.Func;
import io.github.loserya.core.sdk.base.Nested;
import io.github.loserya.core.sdk.base.PageFunc;
import io.github.loserya.core.sdk.base.Update;
import io.github.loserya.hardcode.constant.ECompare;
import io.github.loserya.hardcode.constant.EConditionType;
import io.github.loserya.hardcode.constant.ESortType;
import io.github.loserya.hardcode.constant.EUpdateType;
import io.github.loserya.utils.CollectionUtils;
import io.github.loserya.utils.ConvertUtil;
import io.github.loserya.utils.ExceptionUtils;
import io.github.loserya.utils.func.SFunction;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;


/**
 * 条件构建器
 *
 * @author loser
 * @since 1.0.0
 */
public class LambdaQueryWrapper<T>
        implements
        Update<LambdaQueryWrapper<T>, SFunction<T, ?>>,
        ColumnUpdate<LambdaQueryWrapper<T>>,
        Compare<LambdaQueryWrapper<T>, SFunction<T, ?>>,
        ColumnCompare<LambdaQueryWrapper<T>>,
        Func<T, LambdaQueryWrapper<T>, SFunction<T, ?>>,
        ColumnFunc<T, LambdaQueryWrapper<T>>,
        PageFunc<T, LambdaQueryWrapper<T>, SFunction<T, ?>>,
        Nested<LambdaQueryWrapper<T>, LambdaQueryWrapper<T>> {

    private final List<SelectField> fields = new ArrayList<>(5);
    private final List<Condition> conditions = new ArrayList<>(5);
    private final List<SortCondition> sortConditions = new ArrayList<>(5);
    private final List<UpdateField> updateFields = new ArrayList<>(5);
    private Long skip;
    private Integer limit;

    public void appendCondition(LambdaQueryWrapper<?> wrapper) {

        if (Objects.isNull(wrapper) || CollectionUtils.isEmpty(wrapper.getConditions())) {
            return;
        }
        this.getConditions().addAll(wrapper.getConditions());

    }

    public List<Condition> getConditions() {
        return conditions;
    }

    public int conditionsSize() {
        return conditions.size();
    }

    public ConditionWrapper getCondition() {
        return new ConditionWrapper(fields, conditions, sortConditions, updateFields, skip, limit);
    }

    private String getFieldMeta(SFunction<T, ?> column) {
        return ConvertUtil.convertToFieldName(column);
    }

    @Override
    public LambdaQueryWrapper<T> eq(boolean condition, SFunction<T, ?> column, Object val) {
        return eq(condition, getFieldMeta(column), val);
    }

    @Override
    public LambdaQueryWrapper<T> ne(boolean condition, SFunction<T, ?> column, Object val) {
        return ne(condition, getFieldMeta(column), val);
    }

    @Override
    public LambdaQueryWrapper<T> le(boolean condition, SFunction<T, ?> column, Object val) {
        return le(condition, getFieldMeta(column), val);
    }

    @Override
    public LambdaQueryWrapper<T> lt(boolean condition, SFunction<T, ?> column, Object val) {
        return lt(condition, getFieldMeta(column), val);
    }

    @Override
    public LambdaQueryWrapper<T> ge(boolean condition, SFunction<T, ?> column, Object val) {
        return ge(condition, getFieldMeta(column), val);
    }

    @Override
    public LambdaQueryWrapper<T> gt(boolean condition, SFunction<T, ?> column, Object val) {
        return ge(condition, getFieldMeta(column), val);
    }

    @Override
    public LambdaQueryWrapper<T> between(boolean condition, SFunction<T, ?> column, Object leftV, Object rightV) {
        return between(condition, getFieldMeta(column), leftV, rightV);
    }

    @Override
    public LambdaQueryWrapper<T> in(boolean condition, SFunction<T, ?> column, Collection val) {
        return in(condition, getFieldMeta(column), val);
    }

    @Override
    public LambdaQueryWrapper<T> notIn(boolean condition, SFunction<T, ?> column, Collection val) {
        return notIn(condition, getFieldMeta(column), val);
    }

    @Override
    public LambdaQueryWrapper<T> or(Function<LambdaQueryWrapper<T>, LambdaQueryWrapper<T>> func) {
        return or(true, func);
    }

    @Override
    public LambdaQueryWrapper<T> and(boolean condition, Function<LambdaQueryWrapper<T>, LambdaQueryWrapper<T>> func) {

        if (condition) {
            LambdaQueryWrapper<T> apply = func.apply(new LambdaQueryWrapper<>());
            if (this.conditions.size() == 0) {
                throw ExceptionUtils.mpe("not first use and");
            }
            ConditionWrapper conditionWrapper = apply.getCondition();
            Condition c = new Condition();
            c.setConditionWrapper(conditionWrapper);
            this.conditions.add(c);
        }
        return this;

    }

    @Override
    public LambdaQueryWrapper<T> or(boolean condition, Function<LambdaQueryWrapper<T>, LambdaQueryWrapper<T>> func) {

        if (condition) {
            LambdaQueryWrapper<T> apply = func.apply(new LambdaQueryWrapper<>());
            ConditionWrapper conditionWrapper = apply.getCondition();
            Condition c = new Condition();
            c.setConditionType(EConditionType.OR);
            c.setConditionWrapper(conditionWrapper);
            conditions.add(c);
        }
        return this;
    }

    @Override
    public final LambdaQueryWrapper<T> orderByAsc(boolean condition, SFunction<T, ?> column) {

        if (condition) {
            appendSortField(column, ESortType.ASC);
        }
        return this;

    }

    @Override
    public final LambdaQueryWrapper<T> orderByDesc(boolean condition, SFunction<T, ?> column) {
        if (condition) {
            appendSortField(column, ESortType.DESC);
        }
        return this;
    }

    @Override
    public LambdaQueryWrapper<T> skip(Long skip) {
        this.skip = skip;
        return this;
    }

    @Override
    public LambdaQueryWrapper<T> limit(Integer limit) {
        this.limit = limit;
        return this;
    }

    @SafeVarargs
    @Override
    public final LambdaQueryWrapper<T> select(SFunction<T, ?>... columns) {
        if (Objects.nonNull(columns) && columns.length > 0) {
            List<SelectField> fields = Arrays.stream(columns).map(column -> new SelectField(getFieldMeta(column))).collect(Collectors.toList());
            this.fields.addAll(fields);
        }
        return this;
    }

    private void appendUpdateField(SFunction<T, ?> column, Object val, EUpdateType updateType) {
        appendUpdateField(getFieldMeta(column), val, updateType);
    }

    private void appendUpdateField(String column, Object val, EUpdateType updateType) {
        updateFields.add(new UpdateField(column, updateType, val));
    }

    private void appendSortField(SFunction<T, ?> column, ESortType sortType) {
        appendSortField(getFieldMeta(column), sortType);
    }

    private void appendSortField(String column, ESortType sortType) {
        sortConditions.add(new SortCondition(sortType, column));
    }

    @Override
    public LambdaQueryWrapper<T> eq(boolean condition, String column, Object val) {
        if (condition) {
            conditions.add(new Condition(ECompare.EQ, column, Collections.singletonList(val)));
        }
        return this;
    }

    @Override
    public LambdaQueryWrapper<T> ne(boolean condition, String column, Object val) {
        if (condition) {
            conditions.add(new Condition(ECompare.NE, column, Collections.singletonList(val)));
        }
        return this;
    }

    @Override
    public LambdaQueryWrapper<T> le(boolean condition, String column, Object val) {
        if (condition) {
            conditions.add(new Condition(ECompare.LE, column, Collections.singletonList(val)));
        }
        return this;
    }

    @Override
    public LambdaQueryWrapper<T> lt(boolean condition, String column, Object val) {
        if (condition) {
            conditions.add(new Condition(ECompare.LT, column, Collections.singletonList(val)));
        }
        return this;
    }

    @Override
    public LambdaQueryWrapper<T> ge(boolean condition, String column, Object val) {
        if (condition) {
            conditions.add(new Condition(ECompare.GE, column, Collections.singletonList(val)));
        }
        return this;
    }

    @Override
    public LambdaQueryWrapper<T> gt(boolean condition, String column, Object val) {
        if (condition) {
            conditions.add(new Condition(ECompare.GT, column, Collections.singletonList(val)));
        }
        return this;
    }

    @Override
    public LambdaQueryWrapper<T> between(boolean condition, String column, Object leftV, Object rightV) {
        if (condition) {
            conditions.add(new Condition(ECompare.BW, column, Arrays.asList(leftV, rightV)));
        }
        return this;
    }

    @Override
    public LambdaQueryWrapper<T> in(boolean condition, String column, Collection val) {
        if (condition) {
            conditions.add(new Condition(ECompare.IN, column, Collections.singletonList(val)));
        }
        return this;
    }

    @Override
    public LambdaQueryWrapper<T> notIn(boolean condition, String column, Collection val) {
        if (condition) {
            conditions.add(new Condition(ECompare.NIN, column, Collections.singletonList(val)));
        }
        return this;
    }

    @Override
    public LambdaQueryWrapper<T> orderByAsc(boolean condition, String column) {
        if (condition) {
            appendSortField(column, ESortType.ASC);
        }
        return this;
    }

    @Override
    public LambdaQueryWrapper<T> orderByDesc(boolean condition, String column) {
        if (condition) {
            appendSortField(column, ESortType.DESC);
        }
        return this;
    }

    @Override
    public LambdaQueryWrapper<T> select(String... columns) {
        if (Objects.nonNull(columns) && columns.length > 0) {
            List<SelectField> fields = Arrays.stream(columns).map(SelectField::new).collect(Collectors.toList());
            this.fields.addAll(fields);
        }
        return this;
    }

    @Override
    public LambdaQueryWrapper<T> set(boolean condition, String column, Object val) {
        if (condition) {
            appendUpdateField(column, val, EUpdateType.SET);
        }
        return this;
    }

    @Override
    public LambdaQueryWrapper<T> incr(boolean condition, String column, Number val) {
        if (condition) {
            appendUpdateField(column, val, EUpdateType.INCR);
        }
        return this;
    }

    @Override
    public LambdaQueryWrapper<T> decr(boolean condition, String column, Number val) {
        if (condition) {
            appendUpdateField(column, val, EUpdateType.DECR);
        }
        return this;
    }

    @Override
    public LambdaQueryWrapper<T> set(boolean condition, SFunction<T, ?> column, Object val) {
        if (condition) {
            appendUpdateField(column, val, EUpdateType.SET);
        }
        return this;
    }

    @Override
    public LambdaQueryWrapper<T> incr(boolean condition, SFunction<T, ?> column, Number val) {
        if (condition) {
            appendUpdateField(column, val, EUpdateType.INCR);
        }
        return this;
    }

    @Override
    public LambdaQueryWrapper<T> decr(boolean condition, SFunction<T, ?> column, Number val) {
        if (condition) {
            appendUpdateField(column, val, EUpdateType.DECR);
        }
        return this;
    }
}
