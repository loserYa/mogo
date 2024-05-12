package io.github.loserya.module.fill.anno;

import io.github.loserya.module.fill.FieldFillHandler;
import io.github.loserya.module.fill.hardcode.FieldFill;
import org.springframework.data.mongodb.core.mapping.Document;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 注解填充字段
 *
 * @author loser
 * @since 2024/5/12
 */
@Document
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface FieldAutoFill {

    Class<? extends FieldFillHandler<?>> value();

    FieldFill type() default FieldFill.DEFAULT;

}
