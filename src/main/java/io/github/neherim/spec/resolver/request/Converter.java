package io.github.neherim.spec.resolver.request;

public interface Converter {
    <T> T convert(String value, Class<T> expectedClass);
}
