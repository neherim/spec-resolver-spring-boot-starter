package io.github.neherim.web.table.filtering;

import io.github.neherim.web.table.filtering.builder.SpecificationBuilder;
import io.github.neherim.web.table.filtering.request.FilterRequest;
import io.github.neherim.web.table.filtering.request.FilterRequestParser;
import org.springframework.core.MethodParameter;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import java.lang.reflect.ParameterizedType;

public class SpecificationMethodArgumentResolver implements HandlerMethodArgumentResolver {
    private final FilterRequestParser filterRequestParser;
    private final SpecificationBuilder specificationBuilder;

    public SpecificationMethodArgumentResolver(FilterRequestParser filterRequestParser,
                                               SpecificationBuilder specificationBuilder) {
        this.filterRequestParser = filterRequestParser;
        this.specificationBuilder = specificationBuilder;
    }

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.getParameterType().equals(Specification.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter,
                                  ModelAndViewContainer mavContainer,
                                  NativeWebRequest webRequest,
                                  WebDataBinderFactory binderFactory) {
        var entityClass = getEntityType(parameter);

        FilterRequest filterRequest;
        if (isReadParamsFromBody(parameter)) {
            filterRequest = filterRequestParser.fromRequestBody(webRequest);
        } else {
            filterRequest = filterRequestParser.fromRequestParams(webRequest);
        }
        return specificationBuilder.build(entityClass, filterRequest);
    }

    private Class<?> getEntityType(MethodParameter parameter) {
        return (Class<?>) ((ParameterizedType) parameter.getGenericParameterType()).getActualTypeArguments()[0];
    }

    private boolean isReadParamsFromBody(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(RequestBody.class);
    }
}
