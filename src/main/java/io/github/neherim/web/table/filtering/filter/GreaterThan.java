package io.github.neherim.web.table.filtering.filter;

import io.github.neherim.web.table.filtering.filter.common.ComparableSingleValueFilter;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Path;
import jakarta.persistence.criteria.Predicate;

public class GreaterThan<T extends Comparable<? super T>> extends ComparableSingleValueFilter<T> {

    @Override
    public Predicate makePredicate(Path<T> field, T filterValue, CriteriaBuilder cb) {
        return cb.greaterThan(field, filterValue);
    }
}
