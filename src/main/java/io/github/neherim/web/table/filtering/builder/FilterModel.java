package io.github.neherim.web.table.filtering.builder;


import io.github.neherim.web.table.filtering.annotations.Filter;
import io.github.neherim.web.table.filtering.filter.FieldFilter;
import io.github.neherim.web.table.filtering.utils.StringUtils;

public record FilterModel(
        String filterParamName,
        String entityFieldName,
        String paramSeparator,
        Class<? extends FieldFilter> filterClass) {

    FilterModel(String entityFieldName, Filter annotation) {
        this(
                StringUtils.isNotEmpty(annotation.name()) ? annotation.name() : entityFieldName,
                entityFieldName,
                annotation.separator(),
                annotation.filter());
    }
}
