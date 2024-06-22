/**
 * MetaObjectInterceptor.java 代码解读
 * 这段代码是一个Java类，名为MetaObjectInterceptor，它实现了一个接口Interceptor。这个类的主要功能是在数据库操作（如保存、更新）之前，对实体对象的字段进行自动填充。下面是对代码的详细解释：
 * <p>
 * 类定义和接口实现：
 * <p>
 * public class MetaObjectInterceptor implements Interceptor：定义了一个名为MetaObjectInterceptor的公共类，它实现了Interceptor接口。
 * 方法实现：
 * <p>
 * save(Object entity, Class<?> clazz)：在保存单个实体对象之前，调用handleIdGen生成ID，handleFileByAnno根据注解处理字段填充，然后调用handlerMetaObject进行全局字段填充。
 * saveBatch(Collection<?> entityList, Class<?> clazz)：在批量保存实体对象之前，对每个实体执行与save方法相同的操作。
 * update(Object entity, LambdaQueryWrapper<?> queryWrapper, Class<?> clazz)：在更新实体对象之前，调用handleFileByAnno处理字段填充，然后调用handlerMetaObject进行全局字段填充。
 * 私有方法：
 * <p>
 * handlerMetaObject(boolean isSave, Object entity, Class<?> clazz)：处理全局的对象字段填充。根据是保存还是更新操作，调用不同的填充处理器。
 * handleFileByAnno(Object entity, Class<?> clazz, FieldFill... types)：处理单个注解标志的字段填充。根据注解类型，调用相应的处理器填充字段。
 * listFiledFill(Class<?> clazz)：获取指定类的所有需要填充的字段列表。
 * listFiledFill(List<FiledFillResult> fields, Class<?> clazz)：递归地获取所有超类的需要填充的字段列表。
 * handleIdGen(Object entity)：处理实体对象的ID生成。
 * 注解和反射：
 * <p>
 * 代码使用了Java反射和注解来动态地处理字段填充。例如，通过FieldAutoFill注解标记的字段会在保存或更新时被自动填充。
 * 异常处理：
 * <p>
 * 在处理字段填充时，如果遇到异常，代码会抛出运行时异常。
 * 缓存和效率：
 * <p>
 * 代码中使用了MeatObjectCache来缓存处理器和字段信息，以提高效率。
 * 泛型和集合操作：
 * <p>
 * 方法saveBatch和listFiledFill使用了Java的泛型和集合操作来处理多个实体对象和字段。
 * 总的来说，这个类是一个数据库操作拦截器，用于在实体对象被保存或更新到数据库之前，自动填充其字段。它利用Java反射和注解来实现这一功能，提高了代码的灵活性和可维护性。
 */
package io.github.loserya.module.fill;

import io.github.loserya.core.wrapper.LambdaQueryWrapper;
import io.github.loserya.function.interceptor.Interceptor;
import io.github.loserya.global.cache.MeatObjectCache;
import io.github.loserya.module.fill.anno.FieldAutoFill;
import io.github.loserya.module.fill.entity.FiledFillResult;
import io.github.loserya.module.fill.entity.FiledMeta;
import io.github.loserya.module.fill.hardcode.FieldFill;
import io.github.loserya.module.idgen.IdGenHandler;
import io.github.loserya.utils.ClassUtil;
import io.github.loserya.utils.CollectionUtils;
import io.github.loserya.utils.ExceptionUtils;
import io.github.loserya.utils.StringUtils;

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

        handleIdGen(entity);
        handleFileByAnno(entity, clazz, FieldFill.INSERT, FieldFill.INSERT_UPDATE);
        handlerMetaObject(true, entity, clazz);
        return Interceptor.super.save(entity, clazz);

    }

    @Override
    public Object[] saveBatch(Collection<?> entityList, Class<?> clazz) {

        entityList.forEach(entity -> {
            handleIdGen(entity);
            handleFileByAnno(entity, clazz, FieldFill.INSERT, FieldFill.INSERT_UPDATE);
            handlerMetaObject(true, entity, clazz);
        });
        return Interceptor.super.saveBatch(entityList, clazz);

    }

    @Override
    public Object[] update(Object entity, LambdaQueryWrapper<?> queryWrapper, Class<?> clazz) {

        handleFileByAnno(entity, clazz, FieldFill.UPDATE, FieldFill.INSERT_UPDATE);
        handlerMetaObject(false, entity, clazz);
        return Interceptor.super.update(entity, queryWrapper, clazz);

    }

    @Override
    public Object[] lambdaUpdate(LambdaQueryWrapper<?> queryWrapper, Class<?> clazz) {
        handleLambdaByAnno(queryWrapper, clazz, FieldFill.UPDATE, FieldFill.INSERT_UPDATE);
        handlerLambdaMetaObject(queryWrapper, clazz);
        return Interceptor.super.lambdaUpdate(queryWrapper, clazz);
    }

    private void handleLambdaByAnno(LambdaQueryWrapper<?> queryWrapper, Class<?> clazz, FieldFill... types) {

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
                    org.springframework.data.mongodb.core.mapping.Field collectionField = field.getAnnotation(org.springframework.data.mongodb.core.mapping.Field.class);
                    String column = Objects.nonNull(collectionField) && StringUtils.isNotBlank(collectionField.value()) ? collectionField.value() : field.getName();
                    try {
                        FieldFillHandler<?> handler = MeatObjectCache.HANDLER_MAP.get(item.getHandlerClazz());
                        if (Objects.nonNull(handler)) {
                            queryWrapper.set(column, handler.invoke());
                        }
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                });

    }

    private void handlerLambdaMetaObject(LambdaQueryWrapper<?> queryWrapper, Class<?> clazz) {

        MeatObjectCache.handlers.forEach(handler -> {
            FiledMeta filedMeta = handler.updateFill(clazz, new Object());
            if (Objects.nonNull(filedMeta) && Objects.nonNull(filedMeta.getFiledName()) && Objects.nonNull(filedMeta.getObj())) {
                try {
                    Field field = ClassUtil.getFieldWitchCache(clazz, filedMeta.getFiledName());
                    org.springframework.data.mongodb.core.mapping.Field collectionField = field.getAnnotation(org.springframework.data.mongodb.core.mapping.Field.class);
                    String column = Objects.nonNull(collectionField) && StringUtils.isNotBlank(collectionField.value()) ? collectionField.value() : field.getName();
                    queryWrapper.set(column, filedMeta.getObj());
                } catch (Exception e) {
                    throw ExceptionUtils.mpe(e);
                }
            }
        });

    }

    /**
     * 处理全局的对象字段填充
     */
    private static void handlerMetaObject(boolean isSave, Object entity, Class<?> clazz) {

        MeatObjectCache.handlers.forEach(handler -> {
            List<FiledMeta> metaList;
            if (isSave) {
                metaList = handler.insertFills(clazz, entity);
            } else {
                metaList = handler.updateFills(clazz, entity);
            }
            for (FiledMeta filedMeta : metaList) {
                if (Objects.nonNull(filedMeta) && Objects.nonNull(filedMeta.getFiledName()) && Objects.nonNull(filedMeta.getObj())) {
                    try {
                        Field field = ClassUtil.getFieldWitchCache(clazz, filedMeta.getFiledName());
                        field.set(entity, filedMeta.getObj());
                    } catch (Exception e) {
                        throw ExceptionUtils.mpe(e);
                    }
                }
            }
        });

    }

    /**
     * 处理单个注解标志的字段填充
     */
    private static void handleFileByAnno(Object entity, Class<?> clazz, FieldFill... types) {

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
                        if (Objects.isNull(handler)) {
                            throw ExceptionUtils.mpe("%s is un register in ioc", item.getHandlerClazz().getName());
                        }
                        field.set(entity, handler.invoke());
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

    private void handleIdGen(Object entity) {
        IdGenHandler.setId(entity);
    }

}
