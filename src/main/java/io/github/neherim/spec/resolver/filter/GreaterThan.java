package io.github.neherim.spec.resolver.filter;

import io.github.neherim.spec.resolver.filter.common.ComparableSingleValueFilter;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Path;
import jakarta.persistence.criteria.Predicate;

public class GreaterThan extends ComparableSingleValueFilter {

    @Override
    public <T extends Comparable<? super T>> Predicate makeComparablePredicate(Path<? extends T> field, T filterValue, CriteriaBuilder cb) {
        return cb.greaterThan(field, filterValue);
    }
}
