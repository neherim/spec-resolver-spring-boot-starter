package io.github.neherim.web.table.filtering.builder;

import io.github.neherim.web.table.filtering.annotations.Filter;
import io.github.neherim.web.table.filtering.filter.FieldFilter;
import io.github.neherim.web.table.filtering.request.FilterRequest;
import io.github.neherim.web.table.filtering.utils.StringUtils;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.data.jpa.domain.Specification;

import java.lang.reflect.InvocationTargetException;

public class SpecificationBuilder {
    private final EntityClassParser entityClassParser;

    public SpecificationBuilder(EntityClassParser entityClassParser) {
        this.entityClassParser = entityClassParser;
    }

    public Specification<Object> build(Class<?> entityClass, FilterRequest filterRequest) {
        var filters = entityClassParser.parseClass(entityClass);
        return filters.stream()
                .map(filter -> this.createSpecificationForField(filter.annotation(), filter.entityFieldName(), filterRequest))
                .reduce(Specification::and).orElse(null);
    }

    private Specification<Object> createSpecificationForField(Filter annotation, String entityFieldName, FilterRequest filterRequest) {
        try {
            var fieldFilter = annotation.filter().getConstructor().newInstance();
            return new SpecificationContainer<>(annotation, entityFieldName, filterRequest, fieldFilter);
        } catch (InvocationTargetException | InstantiationException | IllegalAccessException |
                 NoSuchMethodException ex) {
            throw new RuntimeException(ex);
        }
    }

    private static class SpecificationContainer<T> implements Specification<T> {
        private final Filter annotation;
        private final String entityFieldName;
        private final FilterRequest filterRequest;
        private final FieldFilter<T> fieldFilter;

        public SpecificationContainer(Filter annotation,
                                      String entityFieldName,
                                      FilterRequest filterRequest,
                                      FieldFilter<T> fieldFilter) {
            this.annotation = annotation;
            this.entityFieldName = entityFieldName;
            this.filterRequest = filterRequest;
            this.fieldFilter = fieldFilter;
        }

        @Override
        public Predicate toPredicate(Root<T> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
            var filterParamName = StringUtils.isEmpty(annotation.name()) ? annotation.name() : entityFieldName;
            return fieldFilter.makePredicate(annotation, filterParamName, filterRequest, root.get(entityFieldName), cb);
        }
    }
}
