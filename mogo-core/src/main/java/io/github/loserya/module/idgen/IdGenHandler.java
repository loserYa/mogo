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
