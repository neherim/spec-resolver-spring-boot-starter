package io.github.neherim.web.table.filtering.request.converter;

public interface Converter {
    <T> T convert(String value, Class<T> expectedClass);
}
