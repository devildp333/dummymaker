package io.dummymaker.factory.impl;

import io.dummymaker.annotation.ComplexGen;
import io.dummymaker.annotation.PrimeGen;
import io.dummymaker.annotation.special.GenEmbedded;
import io.dummymaker.annotation.special.GenEnumerate;
import io.dummymaker.container.impl.GenContainer;
import io.dummymaker.container.impl.GeneratorsStorage;
import io.dummymaker.factory.IPopulateFactory;
import io.dummymaker.generator.complex.IComplexGenerator;
import io.dummymaker.generator.simple.IGenerator;
import io.dummymaker.generator.simple.impl.EmbeddedGenerator;
import io.dummymaker.scan.IAnnotationScanner;
import io.dummymaker.scan.IPopulateScanner;
import io.dummymaker.scan.impl.EnumerateScanner;
import io.dummymaker.util.BasicCastUtils;
import io.dummymaker.util.BasicCollectionUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.*;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import static io.dummymaker.util.BasicCastUtils.castObject;
import static io.dummymaker.util.BasicCastUtils.instantiate;

/**
 * Scan for populate annotations on entity fields
 * and generate values for such fields via generators
 *
 * @see IGenerator
 * @see IComplexGenerator
 * @see PrimeGen
 * @see ComplexGen
 * @see GenEnumerate
 *
 * @author GoodforGod
 * @since 10.03.2018
 */
abstract class BasicPopulateFactory implements IPopulateFactory {

    private static final Logger logger = Logger.getLogger(GenPopulateFactory.class.getName());

    private static final int MAX_EMBEDDED_DEPTH = GenEmbedded.MAX;
    private static final int MIN_EMBEDDED_DEPTH = 1;

    private final IAnnotationScanner enumerateScanner;
    private final IPopulateScanner populateScanner;

    private final GeneratorsStorage genStorage;

    BasicPopulateFactory(final IPopulateScanner populateScanner) {
        this.genStorage = new GeneratorsStorage();
        this.enumerateScanner = new EnumerateScanner();
        this.populateScanner = populateScanner;
    }

    /**
     * Populate single entity
     *
     * @param t                    entity to populate
     * @param enumeratesMap        map of enumerated marked fields
     * @param nullableFields       set with fields that had errors in
     * @param currentEmbeddedDepth embedded depth level
     * @return populated entity
     */
    private <T> T populateEntity(final T t,
                                 final Map<Field, Long> enumeratesMap,
                                 final Set<Field> nullableFields,
                                 final int currentEmbeddedDepth) {
        final Map<Field, GenContainer> genContainers = this.populateScanner.scan(t.getClass());
        for (final Map.Entry<Field, GenContainer> annotatedField : genContainers.entrySet()) {
            final Field field = annotatedField.getKey();
            // If field had errors or null gen in prev populate iteration, just skip that field
            if (nullableFields.contains(field))
                continue;

            try {
                field.setAccessible(true);
                final Object objValue = generateObject(field,
                        annotatedField.getValue(),
                        enumeratesMap,
                        nullableFields,
                        currentEmbeddedDepth);
                field.set(t, objValue);
            } catch (ClassCastException e) {
                logger.warning(e.getMessage() + " | field TYPE and GENERATE TYPE are not compatible");
                nullableFields.add(field); // skip field due to error as if it null
                throw e;
            } catch (IllegalAccessException e) {
                logger.warning(e.getMessage() + " | have NO ACCESS to field: " + field.getName());
                nullableFields.add(field); // skip field due to error as if it null
            } catch (Exception e) {
                logger.warning(e.getMessage());
                nullableFields.add(field); // skip field due to error as if it null
            } finally {
                annotatedField.getKey().setAccessible(false);
            }
        }
        return t;
    }

    /**
     * Generate populate field value
     *
     * @param field          field to populate
     * @param container      field populate annotations
     * @param enumerateMap   field enumerate map
     * @param nullableFields set with fields that had errors in
     */
    private Object generateObject(final Field field,
                                  final GenContainer container,
                                  final Map<Field, Long> enumerateMap,
                                  final Set<Field> nullableFields,
                                  final int currentEmbeddedDepth) {
        final IGenerator generator = genStorage.getGenInstance(container.getGeneratorClass());
        final Annotation annotation = container.getMarker();

        Object generated;

        if (EmbeddedGenerator.class.equals(container.getGeneratorClass())) {
            generated = generateEmbeddedObject(annotation, field, nullableFields, currentEmbeddedDepth);
        } else if (enumerateMap.containsKey(field)) {
            generated = generateEnumerateObject(field, enumerateMap);
        } else if (container.isComplex()) {
            // If complexGen can generate embedded objects
            // And not handling it like BasicComplexGenerator, you are StackOverFlowed
            generated = ((IComplexGenerator) generator).generate(annotation, field, genStorage, currentEmbeddedDepth);
        } else {
            generated = generator.generate();
        }

        final Object casted = castObject(generated, field.getType());
        if (casted == null)
            nullableFields.add(field);

        return casted;
    }

    /**
     * Generate embedded field value
     *
     * @param field          field with embedded value
     * @param nullableFields set with fields that had errors in
     */
    private Object generateEmbeddedObject(final Annotation annotation,
                                          final Field field,
                                          final Set<Field> nullableFields,
                                          final int currentEmbeddedDepth) {
        final int fieldDepth = getDepth(annotation);
        if (fieldDepth < currentEmbeddedDepth)
            return null;

        final Object embedded = instantiate(field.getType());
        if (embedded == null) {
            nullableFields.add(field);
            return null;
        }

        return populateEntity(embedded,
                buildEnumerateMap(field.getType()),
                buildNullableSet(),
                currentEmbeddedDepth + 1);
    }

    /**
     * Generate enumerate field next value
     */
    private Object generateEnumerateObject(final Field field,
                                           final Map<Field, Long> enumerateMap) {
        final Long currentEnumerateValue = enumerateMap.get(field);
        Object objValue = BasicCastUtils.castToNumber(currentEnumerateValue, field.getType());

        // Increment numerate number for generated field
        enumerateMap.computeIfPresent(field, (k, v) -> v + 1);
        return objValue;
    }

    <T> T populate(final T t,
                   final int depth) {
        if (t == null)
            return null;

        return populateEntity(t,
                buildEnumerateMap(t.getClass()),
                buildNullableSet(),
                depth);
    }

    @Override
    public <T> T populate(final T t) {
        if (t == null)
            return null;

        return populateEntity(t,
                buildEnumerateMap(t.getClass()),
                buildNullableSet(),
                MIN_EMBEDDED_DEPTH);
    }

    @Override
    public <T> List<T> populate(final List<T> list) {
        if (BasicCollectionUtils.isEmpty(list))
            return Collections.emptyList();

        final Class<?> tClass = list.get(0).getClass();

        final Set<Field> nullableFields = buildNullableSet(); // use for performance
        final Map<Field, Long> enumerateMap = buildEnumerateMap(tClass); // store enumerate gen state

        return list.stream()
                .filter(Objects::nonNull)
                .map(t -> populateEntity(t, enumerateMap, nullableFields, MIN_EMBEDDED_DEPTH))
                .collect(Collectors.toList());
    }

    private int getDepth(final Annotation annotation) {
        if (annotation == null || !annotation.annotationType().equals(GenEmbedded.class))
            return MIN_EMBEDDED_DEPTH;

        final int fieldDepth = ((GenEmbedded) annotation).depth();
        if (fieldDepth < 1)
            return MIN_EMBEDDED_DEPTH;

        return (fieldDepth > MAX_EMBEDDED_DEPTH)
                ? MAX_EMBEDDED_DEPTH
                : fieldDepth;
    }

    /**
     * Setup map for enumerate fields
     *
     * @param t class to scan for enumerate fields
     */
    private Map<Field, Long> buildEnumerateMap(final Class t) {
        return this.enumerateScanner.scan(t).entrySet().stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey,
                        e -> ((GenEnumerate) e.getValue().get(0)).from())
                );
    }

    private Set<Field> buildNullableSet() {
        return new HashSet<>();
    }
}
