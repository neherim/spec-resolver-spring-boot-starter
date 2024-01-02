package io.github.neherim.spec.resolver.annotations;


import io.github.neherim.spec.resolver.filter.FieldFilter;

import java.lang.annotation.*;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.TYPE})
@Repeatable(Filters.class)
public @interface Filter {
    /**
     * The name of the parameter from the HTTP request used to filter.
     * If no name is provided, the entity field name is used as default.
     */
    String name() default "";

    /**
     * Filter value separator
     */
    String separator() default "";

    /**
     * Filter specification
     */
    Class<? extends FieldFilter> filter();
}
