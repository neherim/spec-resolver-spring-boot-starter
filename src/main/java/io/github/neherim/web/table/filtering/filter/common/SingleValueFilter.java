package io.github.neherim.web.table.filtering.filter.common;

import io.github.neherim.web.table.filtering.annotations.Filter;
import io.github.neherim.web.table.filtering.filter.FieldFilter;
import io.github.neherim.web.table.filtering.request.FilterRequest;
import io.github.neherim.web.table.filtering.utils.StringUtils;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Path;
import jakarta.persistence.criteria.Predicate;

public abstract class SingleValueFilter<T> implements FieldFilter<T> {

    @Override
    public Predicate makePredicate(Filter annotation, String filterParamName, FilterRequest filterRequest,
                                   Path<T> entityField, CriteriaBuilder cb) {
        var filterValue = filterRequest.getParamValue(filterParamName, entityField.getJavaType());
        if (filterValue == null) {
            return null;
        }
        return makePredicate(entityField, filterValue, cb);
    }

    public abstract Predicate makePredicate(Path<T> field, T filterValue, CriteriaBuilder cb);
}
