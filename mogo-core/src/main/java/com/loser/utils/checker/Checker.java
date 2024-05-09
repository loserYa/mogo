package com.loser.utils.checker;

import com.loser.hardcode.exception.MogoException;
import com.loser.utils.ConvertUtil;
import com.loser.utils.ExceptionUtils;
import com.loser.utils.func.SFunction;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * 参数校验工具
 *
 * @author loser
 * @date 2022/7/4 15:05
 */
public class Checker<T> {

    private List<Condition> conditions = new ArrayList<>();

    /**
     * 判断该值不能为传入值
     */
    public Checker<T> ne(SFunction<T, ?> column, Object val) {
        conditions.add(new Condition(getFieldMeta(column), ECheckType.NE, val));
        return this;
    }

    /**
     * 判断值不能为空
     */
    public Checker<T> notNull(SFunction<T, ?> column) {
        conditions.add(new Condition(getFieldMeta(column), ECheckType.NOTNULL, null));
        return this;
    }

    /**
     * 不能为空且不能为传入值
     */
    public Checker<T> notNe(SFunction<T, ?> column, Object val) {
        conditions.add(new Condition(getFieldMeta(column), ECheckType.NOTNULL_NE, val));
        return this;
    }

    /**
     * 执行校验逻辑
     */
    public void check(T obj) {

        if (Objects.isNull(obj)) {
            throw ExceptionUtils.mpe("参数为空");
        }
        Class<?> targetClass = obj.getClass();
        for (Condition condition : conditions) {
            ECheckType type = condition.getType();
            try {
                Field field = targetClass.getDeclaredField(condition.getField());
                field.setAccessible(true);
                switch (type) {
                    case NE:
                        if (Objects.equals(field.get(obj), condition.getVal())) {
                            throw ExceptionUtils.mpe(String.format("字段(%s)赋值错误", condition.getField()));
                        }
                        break;
                    case NOTNULL:
                        if (Objects.isNull(field.get(obj))) {
                            throw ExceptionUtils.mpe(String.format("字段(%s)为空", condition.getField()));
                        }
                        break;
                    case NOTNULL_NE:
                        Object result = field.get(obj);
                        if (Objects.isNull(result) || result.equals(condition.getVal())) {
                            throw ExceptionUtils.mpe(String.format("字段(%s)值错误", condition.getField()));
                        }
                        break;
                    default:
                }
            } catch (MogoException e) {
                throw e;
            } catch (Exception e) {
                throw ExceptionUtils.mpe(String.format("字段(%s)不存在", condition.getField()));
            }
        }
    }

    private String getFieldMeta(SFunction<T, ?> column) {
        return ConvertUtil.convertToFieldName(column);
    }


}