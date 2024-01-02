package io.github.neherim.spec.resolver.filter.common;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Path;
import jakarta.persistence.criteria.Predicate;

public abstract class StringValueFilter extends SingleValueFilter {

    @Override
    public <T> Predicate makeSingleValuePredicate(Path<T> field, T filterValue, CriteriaBuilder cb) {
        return makeStringPredicate((Path<String>) field, (String) filterValue, cb);
    }

    public abstract Predicate makeStringPredicate(Path<String> field, String filterValue, CriteriaBuilder cb);
}
