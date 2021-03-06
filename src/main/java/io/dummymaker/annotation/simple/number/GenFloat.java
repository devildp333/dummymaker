package io.dummymaker.annotation.simple.number;

import io.dummymaker.annotation.PrimeGen;
import io.dummymaker.generator.simple.impl.number.FloatGenerator;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @see FloatGenerator
 *
 * @author GoodforGod
 * @since 04.11.2018
 */
@PrimeGen(FloatGenerator.class)
@Retention(value = RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface GenFloat {

}
