package io.github.indesil.beren.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import static io.github.indesil.beren.annotations.SourceType.METHOD_DEFINITION;

@Retention(RetentionPolicy.CLASS)
@Target(ElementType.ANNOTATION_TYPE)
public @interface Source {
    SourceType type() default METHOD_DEFINITION;
    String value() default "";
}
