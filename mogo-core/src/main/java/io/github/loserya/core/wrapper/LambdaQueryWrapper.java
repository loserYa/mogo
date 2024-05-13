package io.github.loserya.core.wrapper;

import io.github.loserya.core.entity.Condition;
import io.github.loserya.core.entity.SelectField;
import io.github.loserya.core.entity.SortCondition;
import io.github.loserya.core.sdk.base.ColumnCompare;
import io.github.loserya.core.sdk.base.ColumnFunc;
import io.github.loserya.core.sdk.base.Compare;
import io.github.loserya.core.sdk.base.Func;
import io.github.loserya.core.sdk.base.Nested;
import io.github.loserya.core.sdk.base.PageFunc;
import io.github.loserya.hardcode.constant.ECompare;
import io.github.loserya.hardcode.constant.EConditionType;
import io.github.loserya.hardcode.constant.ESortType;
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
        Compare<LambdaQueryWrapper<T>, SFunction<T, ?>>,
        ColumnCompare<LambdaQueryWrapper<T>>,
        Func<T, LambdaQueryWrapper<T>, SFunction<T, ?>>,
        ColumnFunc<T, LambdaQueryWrapper<T>>,
        PageFunc<T, LambdaQueryWrapper<T>, SFunction<T, ?>>,
        Nested<LambdaQueryWrapper<T>, LambdaQueryWrapper<T>> {

    private final List<SelectField> fields = new ArrayList<>(5);
    private final List<Condition> conditions = new ArrayList<>(5);
    private final List<SortCondition> sortConditions = new ArrayList<>(5);
    private Long skip;
    private Integer limit;

    public int conditionsSize() {
        return conditions.size();
    }

    public ConditionWrapper getCondition() {
        return new ConditionWrapper(fields, conditions, sortConditions, skip, limit);
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

}
