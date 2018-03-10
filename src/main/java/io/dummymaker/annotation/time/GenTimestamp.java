package io.dummymaker.annotation.time;

import io.dummymaker.annotation.PrimeGen;
import io.dummymaker.generator.impl.time.impl.TimestampGenerator;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @see TimestampGenerator
 *
 * @author GoodforGod
 * @since 21.02.2018
 */
@PrimeGen(TimestampGenerator.class)
@Retention(value = RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface GenTimestamp {

}
