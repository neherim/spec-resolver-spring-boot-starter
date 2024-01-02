package io.github.neherim.web.table.filtering.filter;

import io.github.neherim.web.table.filtering.annotations.Filter;
import io.github.neherim.web.table.filtering.request.FilterRequest;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Path;
import jakarta.persistence.criteria.Predicate;

public interface FieldFilter<F> {
    Predicate makePredicate(Filter annotation, String filterParamName, FilterRequest filterRequest, Path<F> entityField, CriteriaBuilder cb);
}
