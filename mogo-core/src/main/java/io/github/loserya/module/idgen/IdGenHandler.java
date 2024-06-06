/**
 * IdGenHandler.java 代码解读
 * 这段代码是Java语言编写的，属于一个ID生成处理模块。它定义了一个接口IdGenHandler，其中包含一个静态方法setId，用于为实体对象设置ID。下面是对这段代码的详细解释：
 * <p>
 * 导入语句：代码首先导入了所需的类和接口。这些导入的类和接口用于实现ID生成、缓存策略、反射操作以及异常处理等功能。
 * <p>
 * 接口定义：IdGenHandler是一个接口，它定义了一个静态方法setId。
 * <p>
 * 静态方法setId：
 * <p>
 * 参数：Object entity，表示需要设置ID的实体对象。
 * 功能：此方法用于为传入的实体对象设置ID。
 * 实现步骤：
 * 首先检查传入的实体对象是否为null，如果是，则直接返回，不执行任何操作。
 * 使用ClassUtil.getIdField(entity)方法获取实体对象的ID字段（假设每个实体类都有一个特定的ID字段）。
 * 获取ID字段上的IdType注解，这个注解用于指定ID生成的类型。
 * 如果注解存在且其值不等于GEN.OBJECT_ID，则继续执行。
 * 从IdGenStrategyCache.MAP中获取与注解值对应的ID生成策略处理器。
 * 如果找到了相应的处理器，则调用其genId方法为实体生成ID。
 * 如果成功生成了ID，则尝试将这个ID设置到实体的ID字段上。
 * 如果在设置ID时发生异常，则使用ExceptionUtils.mpe(e)方法包装并抛出异常。
 * 异常处理：在设置ID时可能会遇到反射操作相关的异常，例如IllegalAccessException。这些异常被捕获并使用ExceptionUtils.mpe(e)方法进行包装后抛出，以便于错误追踪和处理。
 * <p>
 * 可选性处理：代码中使用了Optional类来优雅地处理可能为null的情况，例如在获取ID生成策略处理器和生成的ID时。
 * <p>
 * 总的来说，这段代码提供了一种灵活的方式来为不同的实体对象生成并设置ID，支持通过注解来指定ID生成策略，并且能够处理相关的异常情况。
 */
package io.github.loserya.module.idgen;

import io.github.loserya.global.cache.IdGenStrategyCache;
import io.github.loserya.module.idgen.anno.IdType;
import io.github.loserya.module.idgen.hardcode.GEN;
import io.github.loserya.utils.ClassUtil;
import io.github.loserya.utils.ExceptionUtils;

import java.lang.reflect.Field;
import java.util.Objects;
import java.util.Optional;

public interface IdGenHandler {

    static void setId(Object entity) {

        if (Objects.isNull(entity)) {
            return;
        }
        Field idField = ClassUtil.getIdField(entity);
        IdType annotation = idField.getAnnotation(IdType.class);
        if (Objects.nonNull(annotation)) {
            if (annotation.value().equals(GEN.OBJECT_ID)) {
                return;
            }
            Optional.ofNullable(IdGenStrategyCache.MAP.get(annotation.value()))
                    .ifPresent(handler -> {
                        Object id = handler.genId(entity);
                        Optional.ofNullable(id).ifPresent(idValue -> {
                            try {
                                idField.set(entity, id);
                            } catch (Exception e) {
                                throw ExceptionUtils.mpe(e);
                            }
                        });
                    });
        }
    }

}
