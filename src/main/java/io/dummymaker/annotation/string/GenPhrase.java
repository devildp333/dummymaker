package io.dummymaker.annotation.string;

import io.dummymaker.annotation.PrimeGenAnnotation;
import io.dummymaker.generator.impl.string.PhraseGenerator;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @see PhraseGenerator
 *
 * @author GoodforGod (Anton Kurako)
 * @since 07.06.2017
 */
@PrimeGenAnnotation(PhraseGenerator.class)
@Retention(value = RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface GenPhrase {

}