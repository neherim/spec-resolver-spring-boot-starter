package io.github.neherim.spec.resolver.model;

import io.github.neherim.spec.resolver.annotations.Filter;
import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public class FilterModelBuilder {

    private final Map<Class<?>, List<FilterModel>> cache = new ConcurrentHashMap<>();

    public <T> List<FilterModel> fromClass(Class<T> clazz) {
        return cache.computeIfAbsent(clazz, this::parseClassInternal);
    }

    private <T> List<FilterModel> parseClassInternal(Class<T> clazz) {
        return FilteredEntityParser.getAllFilteredFields(clazz).stream()
                .map(filteredField -> createFilterModel(filteredField.field(), filteredField.annotation()))
                .toList();
    }

    private FilterModel createFilterModel(Field field, Filter annotation) {
        var filterParamName = StringUtils.isNotEmpty(annotation.name()) ? annotation.name() : field.getName();
        var paramSeparator = StringUtils.isNotEmpty(annotation.separator()) ? annotation.separator() : null;
        return new FilterModel(filterParamName, field.getName(),
                Optional.ofNullable(paramSeparator), annotation.filter());
    }
}
