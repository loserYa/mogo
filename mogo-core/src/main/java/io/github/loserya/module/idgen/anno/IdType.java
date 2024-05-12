package io.github.loserya.module.idgen.anno;

import io.github.loserya.module.idgen.hardcode.GEN;
import org.springframework.data.mongodb.core.mapping.Document;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * id 生成策略
 *
 * @author loser
 * @since 2024/5/12
 */
@Document
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface IdType {

    GEN value();

}
