package io.github.neherim.spec.resolver;

import io.github.neherim.spec.resolver.filter.FieldFilter;
import io.github.neherim.spec.resolver.model.FilterModel;
import io.github.neherim.spec.resolver.model.FilterModelBuilder;
import io.github.neherim.spec.resolver.request.FilterRequest;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.data.jpa.domain.Specification;

import java.lang.reflect.InvocationTargetException;

public class SpecificationBuilder {
    private final FilterModelBuilder filterModelBuilder;

    public SpecificationBuilder(FilterModelBuilder filterModelBuilder) {
        this.filterModelBuilder = filterModelBuilder;
    }

    public Specification<Object> build(Class<?> entityClass, FilterRequest filterRequest) {
        var filterModels = filterModelBuilder.fromClass(entityClass);
        return filterModels.stream()
                .map(filterModel -> createSpecificationForField(filterModel, filterRequest))
                .reduce(Specification::and)
                .orElse(null);
    }

    private Specification<Object> createSpecificationForField(FilterModel filterModel, FilterRequest filterRequest) {
        try {
            var fieldFilter = filterModel.filterClass().getConstructor().newInstance();
            return new SpecificationContainer<>(filterModel, filterRequest, fieldFilter);
        } catch (InvocationTargetException | InstantiationException | IllegalAccessException |
                 NoSuchMethodException ex) {
            throw new IllegalArgumentException(ex);
        }
    }

    private static class SpecificationContainer<T> implements Specification<T> {
        private final FilterModel filterModel;
        private final FilterRequest filterRequest;
        private final FieldFilter fieldFilter;

        public SpecificationContainer(FilterModel filterModel,
                                      FilterRequest filterRequest,
                                      FieldFilter fieldFilter) {
            this.filterModel = filterModel;
            this.filterRequest = filterRequest;
            this.fieldFilter = fieldFilter;
        }

        @Override
        public Predicate toPredicate(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
            return fieldFilter.makePredicate(filterModel, filterRequest, root.get(filterModel.entityFieldName()), cb);
        }
    }
}
