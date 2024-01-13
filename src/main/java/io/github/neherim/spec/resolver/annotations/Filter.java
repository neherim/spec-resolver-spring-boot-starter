package io.github.neherim.spec.resolver.annotations;


import io.github.neherim.spec.resolver.filter.FieldFilter;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.TYPE})
@Repeatable(Filters.class)
public @interface Filter {
    /**
     * The name of the HTTP request parameter used to filter.
     * If no name is provided, the entity field name is used instead.
     */
    String name() default "";

    /**
     * The separator is used if the filter can have multiple values.
     * For example: /search?roleIn=QA,DEVELOPER,PM
     */
    String separator() default "";

    /**
     * Filter specification class
     */
    Class<? extends FieldFilter> filter();
}
