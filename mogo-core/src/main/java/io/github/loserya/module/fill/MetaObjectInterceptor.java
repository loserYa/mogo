package io.github.loserya.module.fill;

import io.github.loserya.core.wrapper.LambdaQueryWrapper;
import io.github.loserya.function.interceptor.Interceptor;
import io.github.loserya.global.cache.MeatObjectCache;
import io.github.loserya.module.fill.anno.FieldAutoFill;
import io.github.loserya.module.fill.entity.FiledFillResult;
import io.github.loserya.module.fill.entity.FiledMeta;
import io.github.loserya.module.fill.hardcode.FieldFill;
import io.github.loserya.utils.ClassUtil;
import io.github.loserya.utils.ExceptionUtils;
import org.springframework.util.CollectionUtils;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;

/**
 * 字段填充拦截器
 *
 * @author loser
 * @since 2024/5/12
 */
public class MetaObjectInterceptor implements Interceptor {

    @Override
    public Object[] save(Object entity, Class<?> clazz) {

        setSaveValue(entity, clazz, FieldFill.INSERT, FieldFill.INSERT_UPDATE);
        handlerMetaObject(true, entity, clazz);
        return Interceptor.super.save(entity, clazz);

    }

    @Override
    public Object[] saveBatch(Collection<?> entityList, Class<?> clazz) {

        entityList.forEach(entity -> {
            setSaveValue(entity, clazz, FieldFill.INSERT, FieldFill.INSERT_UPDATE);
            handlerMetaObject(true, entity, clazz);
        });
        return Interceptor.super.saveBatch(entityList, clazz);

    }

    @Override
    public Object[] updateById(Object entity, Class<?> clazz) {

        setSaveValue(entity, clazz, FieldFill.UPDATE, FieldFill.INSERT_UPDATE);
        handlerMetaObject(false, entity, clazz);
        return Interceptor.super.updateById(entity, clazz);

    }

    @Override
    public Object[] update(Object entity, LambdaQueryWrapper<?> queryWrapper, Class<?> clazz) {

        setSaveValue(entity, clazz, FieldFill.UPDATE, FieldFill.INSERT_UPDATE);
        handlerMetaObject(false, entity, clazz);
        return Interceptor.super.update(entity, queryWrapper, clazz);

    }

    /**
     * 处理全局的对象字段填充
     */
    private static void handlerMetaObject(boolean isSave, Object entity, Class<?> clazz) {

        MeatObjectCache.handlers.forEach(handler -> {
            FiledMeta filedMeta;
            if (isSave) {
                filedMeta = handler.insertFill(clazz, entity);
            } else {
                filedMeta = handler.updateFill(clazz, entity);
            }
            if (Objects.nonNull(filedMeta) && Objects.nonNull(filedMeta.getFiledName()) && Objects.nonNull(filedMeta.getObj())) {
                try {
                    Field field = ClassUtil.getFieldWitchCache(clazz, filedMeta.getFiledName());
                    field.set(entity, filedMeta.getObj());
                } catch (Exception e) {
                    throw ExceptionUtils.mpe(e);
                }
            }
        });

    }

    /**
     * 处理单个注解标志的字段填充
     */
    private static void setSaveValue(Object entity, Class<?> clazz, FieldFill... types) {

        List<FiledFillResult> fillResults = listFiledFill(clazz);
        fillResults.stream().filter(item -> {
                    for (FieldFill type : types) {
                        if (item.getFieldFill().equals(type)) {
                            return true;
                        }
                    }
                    return false;
                })
                .forEach(item -> {
                    Field field = item.getField();
                    try {
                        FieldFillHandler<?> handler = MeatObjectCache.HANDLER_MAP.get(item.getHandlerClazz());
                        if (Objects.nonNull(handler)) {
                            field.set(entity, handler.invoke());
                        }
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                });

    }

    private static List<FiledFillResult> listFiledFill(Class<?> clazz) {

        List<FiledFillResult> results = MeatObjectCache.CLASS_LIST_HASH_MAP.get(clazz);
        if (Objects.nonNull(results)) {
            return results;
        }
        results = listFiledFill(new ArrayList<>(), clazz);
        MeatObjectCache.CLASS_LIST_HASH_MAP.put(clazz, results);
        return results;

    }

    private static List<FiledFillResult> listFiledFill(List<FiledFillResult> fields, Class<?> clazz) {

        if (clazz.equals(Object.class)) {
            return fields;
        }
        Field[] declaredFields = clazz.getDeclaredFields();

        List<FiledFillResult> list = new ArrayList<>();
        for (Field field : declaredFields) {
            FieldAutoFill annotation = field.getAnnotation(FieldAutoFill.class);
            if (Objects.nonNull(annotation)) {
                field.setAccessible(true);
                FiledFillResult result = new FiledFillResult();
                result.setField(field);
                result.setFieldFill(annotation.type());
                result.setHandlerClazz(annotation.value());
                list.add(result);
            }
        }
        if (!CollectionUtils.isEmpty(list)) {
            fields.addAll(list);
        }
        return listFiledFill(fields, clazz.getSuperclass());

    }

}
