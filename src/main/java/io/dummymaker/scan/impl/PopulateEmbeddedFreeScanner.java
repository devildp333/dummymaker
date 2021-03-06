package io.dummymaker.scan.impl;

import io.dummymaker.annotation.ComplexGen;
import io.dummymaker.annotation.PrimeGen;
import io.dummymaker.annotation.complex.*;
import io.dummymaker.annotation.special.GenEmbedded;
import io.dummymaker.container.impl.GenContainer;
import io.dummymaker.generator.simple.impl.EmbeddedGenerator;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * Scanner used by populate factory
 * <p>
 * Scan for prime gen and complex gen annotation and its child annotation
 * EXCLUDE EMBEDDED ANNOTATED FIELDS
 *
 * @see PrimeGen
 * @see ComplexGen
 * @see GenEmbedded
 * @see BasicScanner
 * @see io.dummymaker.factory.IPopulateFactory
 *
 * @author GoodforGod
 * @since 10.03.2018
 */
public class PopulateEmbeddedFreeScanner extends PopulateScanner {

    /**
     * Predicate to check for embedded marker annotation
     * Also check for GenCollection annotations with EmbeddedGenerator
     *
     * @see GenEmbedded
     */
    private final Predicate<Annotation> isNotEmbedded = (a) -> a != null && !a.annotationType().equals(GenEmbedded.class)
            && !(a.annotationType().equals(GenList.class) && ((GenList) a).value().equals(EmbeddedGenerator.class))
            && !(a.annotationType().equals(GenSet.class) && ((GenSet) a).value().equals(EmbeddedGenerator.class))
            && !(a.annotationType().equals(GenMap.class)
            && (((GenMap) a).key().equals(EmbeddedGenerator.class) || ((GenMap) a).value().equals(EmbeddedGenerator.class)))
            && !(a.annotationType().equals(GenArray.class) && ((GenArray) a).value().equals(EmbeddedGenerator.class))
            && !(a.annotationType().equals(GenArray2D.class) && ((GenArray2D) a).value().equals(EmbeddedGenerator.class));

    @Override
    public Map<Field, GenContainer> scan(final Class t) {
        return super.scan(t).entrySet()
                .stream()
                .filter(e -> isNotEmbedded.test(e.getValue().getMarker())
                        || !EmbeddedGenerator.class.equals(e.getValue().getGeneratorClass()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }
}
