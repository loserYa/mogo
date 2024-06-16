package io.github.loserya.core.anno;

import io.github.loserya.core.handler.MapperProxyHandler;
import org.springframework.context.annotation.Import;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
@Documented
@Import(MapperProxyHandler.class)
public @interface MapperProxyScanner {

    String[] value() default {};

}
