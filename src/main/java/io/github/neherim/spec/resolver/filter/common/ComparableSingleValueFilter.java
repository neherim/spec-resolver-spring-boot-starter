package io.github.neherim.spec.resolver.filter.common;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Path;
import jakarta.persistence.criteria.Predicate;

public abstract class ComparableSingleValueFilter extends SingleValueFilter {

    public <T> Predicate makeSingleValuePredicate(Path<T> field, T filterValue, CriteriaBuilder cb) {
        return makeComparablePredicate((Path<? extends Comparable>) field, (Comparable) filterValue, cb);
    }

    public abstract <T extends Comparable<? super T>> Predicate makeComparablePredicate(Path<? extends T> field, T filterValue, CriteriaBuilder cb);
}
