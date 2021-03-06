package io.dummymaker.annotation;

import io.dummymaker.generator.simple.IGenerator;
import io.dummymaker.generator.simple.impl.NullGenerator;
import io.dummymaker.scan.IAnnotationScanner;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Prime annotations, used to create new annotations of specific generator provided type
 * Used as a marker annotation for other annotations
 * <p>
 * Is used by scanners and populate/produce factories
 * <p>
 * This annotation is a core one to support population factory
 *
 * @see IGenerator
 * @see io.dummymaker.scan.IScanner
 * @see IAnnotationScanner
 * @see io.dummymaker.factory.IPopulateFactory
 * @see io.dummymaker.factory.IProduceFactory
 *
 * @author GoodforGod
 * @since 28.05.2017
 */
@Retention(value = RetentionPolicy.RUNTIME)
@Target(value = ElementType.ANNOTATION_TYPE)
public @interface PrimeGen {

    /**
     * Contains generator class to be called to generate values on factory
     *
     * @return generator
     * @see io.dummymaker.factory.IPopulateFactory
     */
    Class<? extends IGenerator> value() default NullGenerator.class;
}
