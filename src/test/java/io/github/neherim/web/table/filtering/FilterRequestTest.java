package io.github.neherim.web.table.filtering;

import io.github.neherim.web.table.filtering.request.FilterRequest;
import io.github.neherim.web.table.filtering.request.converter.Converter;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class FilterRequestTest {

    @Test
    void getParamValue() {
        var requestParams = Map.of(
                "firstName", "Ivan",
                "age", "23",
                "num", "1,2,3");
        var filterRequest = new FilterRequest(requestParams, INT_TYPE_CONVERTER);

        assertEquals("Ivan", filterRequest.getParamValueAsString("firstName"));
        assertEquals(23, filterRequest.getParamValue("age", Integer.class));
        assertEquals(List.of(1, 2, 3), filterRequest.getParamValueAsList("num", ",", Integer.class));

        assertNull(filterRequest.getParamValueAsString("notExist"));
        assertNull(filterRequest.getParamValue("notExist", Integer.class));
        assertTrue(filterRequest.getParamValueAsList("notExist", ",", Integer.class).isEmpty());
    }

    private final Converter INT_TYPE_CONVERTER = new Converter() {
        @Override
        public <T> T convert(String value, Class<T> expectedClass) {
            return expectedClass == Integer.class ? (T) Integer.valueOf(value) : (T) value;
        }
    };
}