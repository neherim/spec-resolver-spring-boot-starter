package io.github.neherim.spec.resolver.model;


import io.github.neherim.spec.resolver.filter.FieldFilter;

import java.util.Optional;

public record FilterModel(
        String filterParamName,
        String entityFieldName,
        Optional<String> paramSeparator,
        Class<? extends FieldFilter> filterClass) {
}
