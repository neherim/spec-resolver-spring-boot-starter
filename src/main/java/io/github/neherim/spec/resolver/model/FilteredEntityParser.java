package io.github.neherim.spec.resolver.model;

import io.github.neherim.spec.resolver.annotations.Filter;
import io.github.neherim.spec.resolver.annotations.Filters;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public class FilteredEntityParser {

    public static List<FilteredField> getAllFilteredFields(Class<?> clazz) {
        Filter[] classLevelFilterAnnotations = clazz.getAnnotationsByType(Filter.class);

        var result = new ArrayList<FilteredField>();
        for (Field field : clazz.getDeclaredFields()) {
            // parse entity fields
            if (field.isAnnotationPresent(Filter.class) || field.isAnnotationPresent(Filters.class)) {
                for (Filter annotation : field.getAnnotationsByType(Filter.class)) {
                    result.add(new FilteredField(field, annotation));
                }
            } else {
                // if field not annotated with Filter, then add class level Filter annotation to field
                for (Filter annotation : classLevelFilterAnnotations) {
                    result.add(new FilteredField(field, annotation));
                }
            }
        }
        return result;
    }

    public record FilteredField(Field field, Filter annotation) {
    }
}
