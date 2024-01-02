package io.github.neherim.spec.resolver.filter;

import io.github.neherim.spec.resolver.model.FilterModel;
import io.github.neherim.spec.resolver.request.FilterRequest;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Path;
import jakarta.persistence.criteria.Predicate;

public interface FieldFilter {
    <T> Predicate makePredicate(FilterModel filterModel, FilterRequest filterRequest, Path<T> entityField, CriteriaBuilder cb);
}

