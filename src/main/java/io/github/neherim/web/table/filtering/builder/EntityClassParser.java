package io.github.neherim.web.table.filtering.builder;

import io.github.neherim.web.table.filtering.annotations.Filter;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class EntityClassParser {

    private final Map<Class<?>, List<FilterModel>> cache = new ConcurrentHashMap<>();

    public <T> List<FilterModel> parseClass(Class<T> clazz) {
        return cache.computeIfAbsent(clazz, this::parseClassInternal);
    }

    private <T> List<FilterModel> parseClassInternal(Class<T> clazz) {
        Filter[] classLevelFilterAnnotations = clazz.getAnnotationsByType(Filter.class);

        var result = new ArrayList<FilterModel>();
        for (Field field : clazz.getDeclaredFields()) {
            // parse entity fields
            if (field.isAnnotationPresent(Filter.class)) {
                for (Filter annotation : field.getAnnotationsByType(Filter.class)) {
                    result.add(new FilterModel(field.getName(), annotation));
                }
            } else {
                // if field not annotated with Filter, then add class level Filter annotation to field
                for (Filter annotation : classLevelFilterAnnotations) {
                    result.add(new FilterModel(field.getName(), annotation));
                }
            }
        }
        return result;
    }
}
