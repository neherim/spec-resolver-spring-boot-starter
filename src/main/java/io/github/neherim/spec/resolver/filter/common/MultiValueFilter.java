package io.github.neherim.spec.resolver.filter.common;

import io.github.neherim.spec.resolver.filter.FieldFilter;
import io.github.neherim.spec.resolver.model.FilterModel;
import io.github.neherim.spec.resolver.request.FilterRequest;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Path;
import jakarta.persistence.criteria.Predicate;

import java.util.List;

public abstract class MultiValueFilter implements FieldFilter {

    @Override
    public <T> Predicate makePredicate(FilterModel filterModel, FilterRequest filterRequest, Path<T> entityField, CriteriaBuilder cb) {
        var filterValues = filterRequest.getParameterList(
                filterModel.filterParamName(),
                filterModel.paramSeparator().orElse(null),
                entityField.getJavaType());
        if (filterValues.isEmpty()) {
            return null;
        }
        return makeMultiValuePredicate(entityField, filterValues, cb);
    }


    public abstract <T> Predicate makeMultiValuePredicate(Path<T> entityField, List<? extends T> filterValues, CriteriaBuilder cb);
}
