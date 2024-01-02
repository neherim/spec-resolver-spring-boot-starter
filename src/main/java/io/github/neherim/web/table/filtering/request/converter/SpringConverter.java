package io.github.neherim.web.table.filtering.request.converter;

import org.springframework.core.convert.ConversionService;

public class SpringConverter implements Converter {
    private final ConversionService conversionService;

    public SpringConverter(ConversionService conversionService) {
        this.conversionService = conversionService;
    }

    @Override
    public <T> T convert(String value, Class<T> expectedClass) {
        if (conversionService.canConvert(String.class, expectedClass)) {
            return conversionService.convert(value, expectedClass);
        } else {
            throw new IllegalArgumentException("TODO");
        }
    }
}
