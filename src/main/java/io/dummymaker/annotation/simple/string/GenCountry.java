package io.dummymaker.annotation.simple.string;

import io.dummymaker.annotation.PrimeGen;
import io.dummymaker.generator.simple.impl.string.CountryGenerator;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @see CountryGenerator
 *
 * @author GoodforGod (Anton Kurako)
 * @since 07.06.2017
 */
@PrimeGen(CountryGenerator.class)
@Retention(value = RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface GenCountry {

}
