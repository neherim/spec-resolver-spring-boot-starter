package io.github.neherim.web.table.filtering.filter.common;

import io.github.neherim.web.table.filtering.annotations.Filter;
import io.github.neherim.web.table.filtering.filter.FieldFilter;
import io.github.neherim.web.table.filtering.request.FilterRequest;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Path;
import jakarta.persistence.criteria.Predicate;

import java.util.List;

public abstract class MultiValueFilter<T> implements FieldFilter<T> {

    public Predicate makePredicate(Filter annotation, String filterParamName, FilterRequest filterRequest,
                                   Path<T> entityField, CriteriaBuilder cb) {
        var filterValues = filterRequest.getParamValueAsList(filterParamName, annotation.separator(), entityField.getJavaType());
        if (filterValues.isEmpty()) {
            return null;
        }
        return makePredicate(entityField, filterValues, cb);
    }

    public abstract Predicate makePredicate(Path<T> entityField, List<? extends T> filterValues, CriteriaBuilder cb);
}
