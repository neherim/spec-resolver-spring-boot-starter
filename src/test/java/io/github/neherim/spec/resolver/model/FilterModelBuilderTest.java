package io.github.neherim.spec.resolver.model;

import io.github.neherim.spec.resolver.annotations.Filter;
import io.github.neherim.spec.resolver.filter.Equal;
import io.github.neherim.spec.resolver.filter.GreaterThan;
import io.github.neherim.spec.resolver.filter.In;
import io.github.neherim.spec.resolver.filter.LikeIgnoreCase;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class FilterModelBuilderTest {

    @Test
    void filterModelFromFieldLevelAnnotation() {
        var filters = new FilterModelBuilder().fromClass(User.class);
        assertEquals(4, filters.size());

        assertFilterDesc("name", "name", LikeIgnoreCase.class, filters.get(0), null);
        assertFilterDesc("age", "ageFrom", GreaterThan.class, filters.get(1), null);
        assertFilterDesc("age", "ageEqual", Equal.class, filters.get(2), null);
        assertFilterDesc("role", "roleIn", In.class, filters.get(3), ",");
    }

    @Test
    void filterModelFromClassLevelAnnotation() {
        var filters = new FilterModelBuilder().fromClass(AnnotatedUser.class);
        assertEquals(3, filters.size());

        assertFilterDesc("name", "name", Equal.class, filters.get(0), null);
        assertFilterDesc("role", "role", Equal.class, filters.get(1), null);
        assertFilterDesc("age", "ageFrom", GreaterThan.class, filters.get(2), null);
    }

    private void assertFilterDesc(String expectedEntityFieldName,
                                  String expectedFilterParamName,
                                  Class<?> expectedFilterClass,
                                  FilterModel filterModel,
                                  String separator) {
        assertEquals(expectedEntityFieldName, filterModel.entityFieldName());
        assertEquals(expectedFilterParamName, filterModel.filterParamName());
        assertEquals(expectedFilterClass, filterModel.filterClass());
        assertEquals(separator, filterModel.paramSeparator().orElse(null));
    }

    static class User {
        @Filter(filter = LikeIgnoreCase.class)
        private String name;
        @Filter(name = "ageFrom", filter = GreaterThan.class)
        @Filter(name = "ageEqual", filter = Equal.class)
        private Integer age;
        @Filter(name = "roleIn", filter = In.class, separator = ",")
        private String role;
    }

    @Filter(filter = Equal.class)
    static class AnnotatedUser {
        private String name;
        private String role;
        @Filter(name = "ageFrom", filter = GreaterThan.class)
        private Integer age;
    }
}