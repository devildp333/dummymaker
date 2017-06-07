package io.dummymaker.annotation;

import io.dummymaker.annotation.prime.PrimeGenAnnotation;
import io.dummymaker.generator.NumerateGenerator;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Generates numeric sequence from given number (default 0) to all produced/populated Dummies
 *
 * @author GoodforGod (Anton Kurako)
 * @since 07.06.2017
 */
@PrimeGenAnnotation(NumerateGenerator.class)
@Retention(value = RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface GenNumerate {
    long from() default 0L;
}
