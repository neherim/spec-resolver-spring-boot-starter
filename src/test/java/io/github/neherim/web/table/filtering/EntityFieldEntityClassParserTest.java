package io.github.neherim.web.table.filtering;

import io.github.neherim.web.table.filtering.annotations.Filter;
import io.github.neherim.web.table.filtering.builder.EntityClassParser;
import io.github.neherim.web.table.filtering.builder.FilterModel;
import io.github.neherim.web.table.filtering.filter.Equal;
import io.github.neherim.web.table.filtering.filter.GreaterThan;
import io.github.neherim.web.table.filtering.filter.LikeIgnoreCase;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class EntityFieldEntityClassParserTest {

    @Test
    void getFilterDescriptionFromFieldLevelAnnotation() {
        var filters = new EntityClassParser().parseClass(User.class);
        assertEquals(3, filters.size());

        assertFilterDesc("name", "name", LikeIgnoreCase.class, filters.get(0));
        assertFilterDesc("age", "ageFrom", GreaterThan.class, filters.get(1));
        assertFilterDesc("age", "ageEqual", Equal.class, filters.get(2));
    }

    @Test
    void getFilterDescriptionFromClassLevelAnnotation() {
        var filters = new EntityClassParser().parseClass(AnnotatedUser.class);
        assertEquals(2, filters.size());

        assertFilterDesc("name", "name", Equal.class, filters.get(0));
        assertFilterDesc("age", "age", Equal.class, filters.get(1));
    }

    private void assertFilterDesc(String expectedFieldName,
                                  String expectedFilterParamName,
                                  Class<?> expectedFilterSpecClass,
                                  FilterModel entityFilter) {
        assertEquals(expectedFieldName, entityFilter.entityFieldName());
        assertEquals(expectedFilterParamName, entityFilter.filterParamName());
        assertEquals(expectedFilterSpecClass, entityFilter.fieldFilterClass());
    }

    static class User {
        @Filter(filter = LikeIgnoreCase.class)
        private String name;
        @Filter(name = "ageFrom", filter = GreaterThan.class)
        @Filter(name = "ageEqual", filter = Equal.class)
        private Integer age;
    }

    @Filter(filter = Equal.class)
    static class AnnotatedUser {
        private String name;
        private Integer age;
    }
}