package io.github.loserya.module.transaction;

import org.springframework.data.mongodb.core.mapping.Document;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Document
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD})
public @interface MogoTransaction {

}
