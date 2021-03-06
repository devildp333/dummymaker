package io.dummymaker.generator.complex.impl;

import io.dummymaker.container.impl.GeneratorsStorage;
import io.dummymaker.generator.simple.IGenerator;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Basic collection complex generator implementation
 * Can be used by other collection complex generators
 *
 * @see io.dummymaker.generator.complex.IComplexGenerator
 *
 * @author GoodforGod
 * @since 22.04.2018
 */
abstract class CollectionComplexGenerator extends BasicComplexGenerator {

    <T> List<T> generateList(final int size,
                             final Class<? extends IGenerator> valueGenerator,
                             final Class<T> fieldClass,
                             final GeneratorsStorage storage,
                             final int depth,
                             final int maxDepth) {

        // Firstly try to generate initial object, so we won't allocate list if not necessary
        final T initial = generateValue(valueGenerator, fieldClass, storage, depth, maxDepth);
        if (initial == null)
            return Collections.emptyList();

        final List<T> list = new ArrayList<>(size);
        list.add(initial);
        for (int i = 0; i < size - 1; i++) {
            final T t = generateValue(valueGenerator, fieldClass, storage, depth, maxDepth);
            list.add(t);
        }

        return list;
    }

    @Override
    public abstract Object generate(final Annotation annotation,
                                    final Field field,
                                    final GeneratorsStorage storage,
                                    final int depth);

    @Override
    public abstract Object generate();
}
