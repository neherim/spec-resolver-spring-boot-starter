package io.github.neherim.web.table.filtering.filter;

import io.github.neherim.web.table.filtering.filter.common.SingleValueFilter;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Path;
import jakarta.persistence.criteria.Predicate;

import java.util.Locale;

public class LikeIgnoreCase extends SingleValueFilter<String> {

    @Override
    public Predicate makePredicate(Path<String> field, String filterValue, CriteriaBuilder cb) {
        var pattern = "%" + filterValue + "%";
        return cb.like(cb.upper(field), pattern.toUpperCase(Locale.ROOT));
    }
}
