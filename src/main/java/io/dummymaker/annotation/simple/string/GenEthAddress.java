package io.dummymaker.annotation.simple.string;

import io.dummymaker.annotation.PrimeGen;
import io.dummymaker.generator.simple.impl.string.EthAddressGenerator;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @see EthAddressGenerator
 *
 * @author GoodforGod
 * @since 04.11.2018
 */
@PrimeGen(EthAddressGenerator.class)
@Retention(value = RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface GenEthAddress {

}
