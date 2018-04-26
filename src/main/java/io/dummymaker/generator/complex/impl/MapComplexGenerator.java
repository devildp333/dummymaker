package io.dummymaker.generator.complex.impl;

import io.dummymaker.annotation.complex.GenMap;
import io.dummymaker.container.impl.GeneratorsStorage;
import io.dummymaker.generator.simple.IGenerator;
import io.dummymaker.generator.simple.impl.string.IdGenerator;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static io.dummymaker.util.BasicCastUtils.getGenericType;
import static io.dummymaker.util.BasicCollectionUtils.generateRandomAmount;
import static io.dummymaker.util.BasicGenUtils.getAutoGenerator;

/**
 * "default comment"
 *
 * @author GoodforGod
 * @since 22.04.2018
 */
public class MapComplexGenerator extends BasicComplexGenerator {

    private Map generateMap(final int amount,
                            final Class<? extends IGenerator> keyGenerator,
                            final Class<? extends IGenerator> valueGenerator,
                            final Class<?> keyFieldType,
                            final Class<?> valueFieldType,
                            final GeneratorsStorage storage) {

        // Firstly try to generate initial object, so we won't allocate list if not necessary
        final Object initialKey     = generateValue(keyGenerator, keyFieldType, storage);
        final Object initialValue   = generateValue(valueGenerator, valueFieldType, storage);
        if(initialKey == null && initialValue == null)
            return Collections.emptyMap();

        final Map map = new HashMap<>(amount);
        map.put(initialKey, initialValue);
        for (int i = 0; i < amount - 1; i++) {
            final Object key    = generateValue(keyGenerator, keyFieldType, storage);
            final Object value  = generateValue(valueGenerator, valueFieldType, storage);

            if (key != null && value != null) {
                map.put(key, value);
            }
        }

        return map;
    }

    @Override
    public Object generate(final Annotation annotation,
                           final Field field,
                           final GeneratorsStorage storage) {
        if (field == null || !field.getType().isAssignableFrom(Map.class))
            return null;

        final Class<?> keyType   = (Class<?>) getGenericType(field.getGenericType(), 0);
        final Class<?> valueType = (Class<?>) getGenericType(field.getGenericType(), 1);
        if(annotation == null) {
            if(storage == null)
                return Collections.emptyMap();

            return generateMap(10,
                    getAutoGenerator(keyType),
                    getAutoGenerator(valueType),
                    keyType,
                    valueType,
                    storage);
        }

        final GenMap a = ((GenMap) annotation);
        final Class<? extends IGenerator> keyGenerator   = a.key();
        final Class<? extends IGenerator> valueGenerator = a.value();

        final int amount = generateRandomAmount(a.min(), a.max(), a.fixed()); // due to initial object

        return generateMap(amount,
                keyGenerator,
                valueGenerator,
                keyType,
                valueType,
                storage);
    }

    @Override
    public Object generate() {
        return generateMap(10,
                IdGenerator.class,
                IdGenerator.class,
                Object.class,
                Object.class,
                null);
    }
}
