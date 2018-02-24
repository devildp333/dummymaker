package io.dummymaker.scan.impl;

import io.dummymaker.annotation.PrimeGenAnnotation;
import io.dummymaker.annotation.special.GenForceExport;
import io.dummymaker.annotation.special.GenIgnoreExport;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;
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

    /**
     * Check for accepted for export annotations
     *
     * @see GenForceExport
     * @see PrimeGenAnnotation
     */
    private final Predicate<Annotation> acceptPredicate = (a) -> (a.annotationType().equals(PrimeGenAnnotation.class)
                                                                || (a.annotationType().equals(GenForceExport.class))
                                                                            && ((GenForceExport) a).value());

    /**
     * Check for ignorable annotations
     *
     * @see GenIgnoreExport
     */
    private final Predicate<Annotation> ignorePredicate = (a) -> (a.annotationType().equals(GenIgnoreExport.class)
                                                                            && ((GenIgnoreExport) a).value());

    @Override
    public Map<Field, List<Annotation>> scan(final Class t) {
        final Map<Field, List<Annotation>> classFieldAnnotations = super.scan(t);

        return (classFieldAnnotations.isEmpty())
                ? classFieldAnnotations
                : classFieldAnnotations.entrySet().stream()
                    .filter(set -> set.getValue().stream().noneMatch(ignorePredicate))
                    .filter(set -> set.getValue().stream().anyMatch(acceptPredicate))
                    .peek(set -> set.setValue(set.getValue().stream()
                            .filter(acceptPredicate)
                            .collect(Collectors.toList())))
                    .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }
}