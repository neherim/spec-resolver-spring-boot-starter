package io.github.neherim.web.table.filtering.filter;

import io.github.neherim.web.table.filtering.filter.common.MultiValueFilter;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Path;
import jakarta.persistence.criteria.Predicate;

import java.util.List;

public class In<T> extends MultiValueFilter<T> {

    @Override
    public Predicate makePredicate(Path<T> entityField, List<? extends T> filterValues, CriteriaBuilder cb) {
        return entityField.in(filterValues);
    }
}
