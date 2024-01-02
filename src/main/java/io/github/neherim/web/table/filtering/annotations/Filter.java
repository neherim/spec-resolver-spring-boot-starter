package io.github.neherim.web.table.filtering.annotations;


import io.github.neherim.web.table.filtering.filter.FieldFilter;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.TYPE})
@Repeatable(Filters.class)
public @interface Filter {
    /**
     * Filter parameter name from request
     */
    String name() default "";


    /**
     * Filter value separator
     */
    String separator() default ",";

    /**
     * Filter specification
     */
    @SuppressWarnings("rawtypes")
    Class<? extends FieldFilter> filter();
}
