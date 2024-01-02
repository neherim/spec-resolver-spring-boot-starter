package io.github.neherim.spec.resolver.filter;

import io.github.neherim.spec.resolver.filter.common.StringValueFilter;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Path;
import jakarta.persistence.criteria.Predicate;

import java.util.Locale;

public class LikeIgnoreCase extends StringValueFilter {

    @Override
    public Predicate makeStringPredicate(Path<String> field, String filterValue, CriteriaBuilder cb) {
        var pattern = "%" + filterValue + "%";
        return cb.like(cb.upper(field), pattern.toUpperCase(Locale.ROOT));
    }
}
