package io.github.loserya.utils;

import io.github.loserya.core.entity.Condition;
import io.github.loserya.core.entity.SortCondition;
import io.github.loserya.core.wrapper.ConditionWrapper;
import io.github.loserya.core.wrapper.LambdaQueryWrapper;
import io.github.loserya.hardcode.constant.ECompare;
import io.github.loserya.hardcode.constant.EConditionType;
import io.github.loserya.hardcode.constant.ESortType;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Field;
import org.springframework.data.mongodb.core.query.Query;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 查询工具构建工具
 *
 * @author loser
 * @since 1.0.0
 */
public class QueryBuildUtils {

    private QueryBuildUtils() {
    }

    private static final Map<ECompare, Function<Condition, Criteria>> HANDLERS = new ConcurrentHashMap<>();

    static {
        HANDLERS.put(ECompare.EQ, QueryBuildUtils::eqHandle);
        HANDLERS.put(ECompare.NE, QueryBuildUtils::neHandle);
        HANDLERS.put(ECompare.LE, QueryBuildUtils::leHandle);
        HANDLERS.put(ECompare.LT, QueryBuildUtils::ltHandle);
        HANDLERS.put(ECompare.GE, QueryBuildUtils::geHandle);
        HANDLERS.put(ECompare.GT, QueryBuildUtils::gtHandle);
        HANDLERS.put(ECompare.BW, QueryBuildUtils::bwHandle);
        HANDLERS.put(ECompare.IN, QueryBuildUtils::inHandle);
        HANDLERS.put(ECompare.NIN, QueryBuildUtils::ninHandle);
    }

    /**
     * 使用条件构建器构建查询条件
     *
     * @param queryWrapper 条件构建器
     * @return 查询条件
     */
    public static Query buildQuery(LambdaQueryWrapper<?> queryWrapper) {
        return buildQuery(queryWrapper.getCondition());
    }

    /**
     * 使用条件构建器构建查询条件
     *
     * @param arg 条件构建器封装的真实条件参数
     * @return 查询条件
     */
    private static Query buildQuery(ConditionWrapper arg) {

        // 01 构建查询参数
        Criteria[] build = buildCondition(arg);
        Query query;
        if (build.length == 1) {
            query = new Query(build[0]);
        } else {
            Criteria criteria = new Criteria();
            criteria.andOperator(build);
            query = new Query(criteria);
        }

        List<SortCondition> sortConditions = arg.getSortConditions();

        // 02 构建排序参数
        if (Objects.nonNull(sortConditions)) {
            query.with(Sort.by(buildSort(sortConditions)));
        }

        // 03 构建分页参数
        if (Objects.nonNull(arg.getSkip())) {
            query.skip(arg.getSkip());
        }
        if (Objects.nonNull(arg.getLimit())) {
            query.limit(arg.getLimit());
        }

        // 04 构建查询列
        if (Objects.nonNull(arg.getFields()) && arg.getFields().size() > 0) {
            Field fields = query.fields();
            arg.getFields().forEach(item -> fields.include(item.getCol()));
        }

        return query;

    }

    /**
     * 构建排序信息
     *
     * @param sortConditions 排序条件
     * @return 排序信息
     */
    private static List<Sort.Order> buildSort(List<SortCondition> sortConditions) {

        return sortConditions.stream().map(item -> {
            if (item.getSortType() == ESortType.ASC) {
                return Sort.Order.asc(item.getCol());
            } else {
                return Sort.Order.desc(item.getCol());
            }
        }).collect(Collectors.toList());

    }

    /**
     * 构建查询条件
     *
     * @param arg 条件构造器中的条件参数
     * @return 查询条件
     */
    private static Criteria[] buildCondition(ConditionWrapper arg) {

        if (Objects.isNull(arg) || Objects.isNull(arg.getConditions()) || arg.getConditions().size() == 0) {
            return new Criteria[]{new Criteria()};
        }
        List<Condition> conditions = arg.getConditions();
        Criteria[] critters = new Criteria[conditions.size()];

        for (int index = 0; index < conditions.size(); index++) {
            Criteria curCriteria;
            Condition condition = conditions.get(index);
            // 处理子集
            if (Objects.nonNull(condition.getConditionWrapper())) {
                curCriteria = buildSubCondition(condition);
            } else {
                curCriteria = buildCurCondition(condition);
            }
            critters[index] = curCriteria;
        }

        return critters;

    }

    private static Criteria buildCurCondition(Condition condition) {

        ECompare type = condition.getType();
        Criteria curCriteria;
        Function<Condition, Criteria> handler = HANDLERS.get(type);
        if (Objects.isNull(handler)) {
            throw ExceptionUtils.mpe(String.format("buildQuery error not have queryType %s", type));
        }
        curCriteria = handler.apply(condition);
        return curCriteria;

    }

    private static Criteria buildSubCondition(Condition condition) {

        Criteria curCriteria;
        curCriteria = new Criteria();
        ConditionWrapper conditionWrapper = condition.getConditionWrapper();
        Criteria[] criteria = buildCondition(conditionWrapper);
        boolean single = criteria.length == 1;
        if (condition.getConditionType().equals(EConditionType.OR)) {
            if (single) {
                curCriteria.orOperator(criteria[0]);
            } else {
                curCriteria.orOperator(criteria);
            }
        } else {
            if (single) {
                curCriteria.andOperator(criteria[0]);
            } else {
                curCriteria.andOperator(criteria);
            }
        }
        return curCriteria;

    }

    /**
     * eq 处理器
     *
     * @param condition 条件参数
     * @return 构建好的查询条件
     */
    private static Criteria eqHandle(Condition condition) {
        return Criteria.where(condition.getCol()).is(condition.getArgs().get(0));
    }

    /**
     * ne 处理器
     *
     * @param condition 条件参数
     * @return 构建好的查询条件
     */
    private static Criteria neHandle(Condition condition) {
        return Criteria.where(condition.getCol()).ne(condition.getArgs().get(0));
    }

    /**
     * le 处理器
     *
     * @param condition 条件参数
     * @return 构建好的查询条件
     */
    private static Criteria leHandle(Condition condition) {
        return Criteria.where(condition.getCol()).lte(condition.getArgs().get(0));
    }

    /**
     * lt 处理器
     *
     * @param condition 条件参数
     * @return 构建好的查询条件
     */
    private static Criteria ltHandle(Condition condition) {
        return Criteria.where(condition.getCol()).lt(condition.getArgs().get(0));
    }

    /**
     * ge 处理器
     *
     * @param condition 条件参数
     * @return 构建好的查询条件
     */
    private static Criteria geHandle(Condition condition) {
        return Criteria.where(condition.getCol()).gte(condition.getArgs().get(0));
    }

    /**
     * gt 处理器
     *
     * @param condition 条件参数
     * @return 构建好的查询条件
     */
    private static Criteria gtHandle(Condition condition) {
        return Criteria.where(condition.getCol()).gt(condition.getArgs().get(0));
    }

    /**
     * between 处理器
     *
     * @param condition 条件参数
     * @return 构建好的查询条件
     */
    private static Criteria bwHandle(Condition condition) {
        return Criteria.where(condition.getCol()).lte(condition.getArgs().get(0)).gt(condition.getArgs().get(1));
    }

    /**
     * in 处理器
     *
     * @param condition 条件参数
     * @return 构建好的查询条件
     */
    private static Criteria inHandle(Condition condition) {
        List<Object> args = (List<Object>) condition.getArgs().get(0);
        return Criteria.where(condition.getCol()).in(args.toArray());
    }

    /**
     * notIn 处理器
     *
     * @param condition 条件参数
     * @return 构建好的查询条件
     */
    private static Criteria ninHandle(Condition condition) {
        List<Object> args = (List<Object>) condition.getArgs().get(0);
        return Criteria.where(condition.getCol()).nin(args.toArray());
    }
}
