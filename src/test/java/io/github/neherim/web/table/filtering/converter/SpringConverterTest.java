package io.github.neherim.web.table.filtering.converter;

import io.github.neherim.web.table.filtering.request.converter.SpringConverter;
import org.junit.jupiter.api.Test;
import org.springframework.format.support.DefaultFormattingConversionService;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;

class SpringConverterTest {
    private final SpringConverter springConverter = new SpringConverter(new DefaultFormattingConversionService());

    @Test
    void successfulValueConversion() {
        var parsedString = springConverter.convert("Hello!", String.class);
        var parsedLocalDate = springConverter.convert("2023-12-12", LocalDate.class);
        var parsedBigDecimal = springConverter.convert("12.34", BigDecimal.class);
        var parsedEnum = springConverter.convert("YELLOW", Color.class);

        assertEquals("Hello!", parsedString);
        assertEquals(LocalDate.of(2023, 12, 12), parsedLocalDate);
        assertEquals(0, new BigDecimal("12.34").compareTo(parsedBigDecimal));
        assertEquals(Color.YELLOW, parsedEnum);
    }

    enum Color {
        YELLOW
    }
}