package io.github.neherim.web.table.filtering.filter;

import io.github.neherim.web.table.filtering.filter.common.SingleValueFilter;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Path;
import jakarta.persistence.criteria.Predicate;

public class Equal<T> extends SingleValueFilter<T> {

    @Override
    public Predicate makePredicate(Path<T> field, T filterValue, CriteriaBuilder cb) {
        return cb.equal(field, filterValue);
    }
}
