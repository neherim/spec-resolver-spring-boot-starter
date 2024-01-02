package io.github.neherim.spec.resolver.request;

import jakarta.annotation.Nullable;
import org.apache.commons.lang3.StringUtils;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class FilterRequest {
    private final Map<String, List<String>> filterParams;
    private final Converter converter;

    public FilterRequest(Map<String, List<String>> filterParams, Converter converter) {
        this.filterParams = filterParams;
        this.converter = converter;
    }

    @Nullable
    public <T> T getParameter(String name, Class<T> clazz) {
        var value = getFirstParamValue(name);
        return convertValue(value, clazz);
    }

    public <T> List<T> getParameterList(String name, String separator, Class<T> clazz) {
        if (separator == null) {
            return getMultiParamValue(name, clazz);
        } else {
            return splitMultiParamValue(name, separator, clazz);
        }
    }

    private <T> List<T> splitMultiParamValue(String name, String separator, Class<T> clazz) {
        return filterParams.getOrDefault(name, Collections.emptyList()).stream()
                .flatMap(value -> Arrays.stream(StringUtils.split(value, separator)))
                .map(value -> convertValue(value, clazz))
                .toList();
    }

    private <T> List<T> getMultiParamValue(String name, Class<T> clazz) {
        return filterParams.getOrDefault(name, Collections.emptyList()).stream()
                .map(value -> convertValue(value, clazz))
                .toList();
    }

    private String getFirstParamValue(String name) {
        var paramValueList = filterParams.get(name);
        if (paramValueList != null && !paramValueList.isEmpty()) {
            return paramValueList.get(0);
        } else {
            return null;
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
