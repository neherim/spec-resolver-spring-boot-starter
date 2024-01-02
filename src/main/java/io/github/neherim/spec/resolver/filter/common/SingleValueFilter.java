package io.github.neherim.spec.resolver.filter.common;

import io.github.neherim.spec.resolver.filter.FieldFilter;
import io.github.neherim.spec.resolver.model.FilterModel;
import io.github.neherim.spec.resolver.request.FilterRequest;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Path;
import jakarta.persistence.criteria.Predicate;

public abstract class SingleValueFilter implements FieldFilter {

    @Override
    public <T> Predicate makePredicate(FilterModel filterModel, FilterRequest filterRequest,
                                       Path<T> entityField, CriteriaBuilder cb) {
        var filterValue = filterRequest.getParameter(filterModel.filterParamName(), entityField.getJavaType());
        if (filterValue == null) {
            return null;
        }
        return makeSingleValuePredicate(entityField, filterValue, cb);
    }

    public abstract <T> Predicate makeSingleValuePredicate(Path<T> field, T filterValue, CriteriaBuilder cb);
}
