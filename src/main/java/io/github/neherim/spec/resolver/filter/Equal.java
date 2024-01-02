package io.github.neherim.spec.resolver.filter;

import io.github.neherim.spec.resolver.filter.common.SingleValueFilter;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Path;
import jakarta.persistence.criteria.Predicate;

public class Equal extends SingleValueFilter {

    @Override
    public <T> Predicate makeSingleValuePredicate(Path<T> field, T filterValue, CriteriaBuilder cb) {
        return cb.equal(field, filterValue);
    }
}
