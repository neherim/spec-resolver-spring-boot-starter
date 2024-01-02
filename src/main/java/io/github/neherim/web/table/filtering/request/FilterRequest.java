package io.github.neherim.web.table.filtering.request;

import io.github.neherim.web.table.filtering.request.converter.Converter;
import jakarta.annotation.Nullable;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class FilterRequest {
    private final Map<String, String> filterParams;
    private final Converter converter;

    public FilterRequest(Map<String, String> filterParams, Converter converter) {
        this.filterParams = filterParams;
        this.converter = converter;
    }

    @Nullable
    public String getParamValueAsString(String paramName) {
        return getParamValue(paramName, String.class);
    }

    @Nullable
    public <T> T getParamValue(String paramName, Class<T> clazz) {
        var value = filterParams.get(paramName);
        return convertValue(value, clazz);
    }

    public <T> List<T> getParamValueAsList(String paramName, String separator, Class<T> clazz) {
        var values = filterParams.get(paramName);
        if (values == null) {
            return Collections.emptyList();
        } else {
            return Arrays.stream(values.split(separator)).map(v -> convertValue(v, clazz)).toList();
        }
    }

    private <T> T convertValue(String value, Class<T> clazz) {
        if (value == null) {
            return null;
        } else if (String.class == clazz) {
            return (T) value;
        } else {
            return converter.convert(value, clazz);
        }
    }
}
