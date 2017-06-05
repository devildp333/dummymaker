package io.dummymaker.scan;

import io.dummymaker.annotation.prime.GenForceExport;
import io.dummymaker.annotation.prime.GenIgnoreExport;
import io.dummymaker.annotation.prime.PrimeGenAnnotation;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.Map;
import java.util.Set;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * Can scan for annotations to export Like all PrimeGenAnnotation annotations and ForceImport also excludes fields with IgnoreExport
 *
 * @see PrimeGenAnnotation
 * @see GenIgnoreExport
 * @see GenForceExport
 *
 * @author GoodforGod
 * @since 03.06.2017
 */
public class ExportAnnotationScanner extends AnnotationScanner {

    private final Predicate<Annotation> acceptPredicate = (a) -> (a.annotationType().equals(PrimeGenAnnotation.class)
                                                                || a.annotationType().equals(GenForceExport.class));

    private final Predicate<Annotation> ignorePredicate = (a) -> a.annotationType().equals(GenIgnoreExport.class);

    @Override
    public Map<Field, Set<Annotation>> scan(Class t) {
        Map<Field, Set<Annotation>> mapToFilter = super.scan(t);

        return (!mapToFilter.isEmpty())
                ? mapToFilter.entrySet().stream()
                    .filter(set -> set.getValue().stream().noneMatch(ignorePredicate))
                    .filter(set -> set.getValue().stream().anyMatch(acceptPredicate))
                    .map(set -> {
                        set.setValue(set.getValue().stream().filter(acceptPredicate).collect(Collectors.toSet()));
                        return set;
                    })
                    .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue))

                : mapToFilter;
    }
}