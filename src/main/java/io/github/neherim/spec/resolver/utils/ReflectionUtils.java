package io.github.neherim.spec.resolver.utils;

import org.springframework.core.MethodParameter;

import java.lang.reflect.ParameterizedType;

public final class ReflectionUtils {
    private ReflectionUtils() {
    }

    public static Class<?> getGenericParameterType(MethodParameter parameter) {
        return (Class<?>) ((ParameterizedType) parameter.getGenericParameterType()).getActualTypeArguments()[0];
    }
}
