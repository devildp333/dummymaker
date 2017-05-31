package io.generator.annotation;

import io.generator.annotation.prime.PrimeGenAnnotation;
import io.generator.generate.DateGenerator;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Default Comment
 *
 * @author @GoodforGod
 * @since 31.05.2017
 */
@PrimeGenAnnotation(DateGenerator.class)
@Retention(value = RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface GenDate {
}
