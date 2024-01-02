package io.github.neherim.spec.resolver.filter;

import io.github.neherim.spec.resolver.filter.common.MultiValueFilter;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.Path;
import jakarta.persistence.criteria.Predicate;

import java.util.List;

public class In extends MultiValueFilter {

    @Override
    public <T> Predicate makeMultiValuePredicate(Path<T> entityField, List<? extends T> filterValues, CriteriaBuilder cb) {
        return entityField.in(filterValues);
    }
}
