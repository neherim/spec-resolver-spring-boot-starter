package io.github.neherim.spec.resolver;

import io.github.neherim.spec.resolver.model.FilterModelBuilder;
import io.github.neherim.spec.resolver.request.Converter;
import io.github.neherim.spec.resolver.request.FilterRequestParamParser;
import io.github.neherim.spec.resolver.utils.ReflectionUtils;
import org.springframework.core.MethodParameter;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.format.support.DefaultFormattingConversionService;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

public class SpecificationMethodArgumentResolver implements HandlerMethodArgumentResolver {
    private final FilterRequestParamParser filterRequestParamParser;
    private final SpecificationBuilder specificationBuilder;

    public SpecificationMethodArgumentResolver(FilterRequestParamParser filterRequestParamParser,
                                               SpecificationBuilder specificationBuilder) {
        this.filterRequestParamParser = filterRequestParamParser;
        this.specificationBuilder = specificationBuilder;
    }

    public SpecificationMethodArgumentResolver(Converter converter) {
        this.filterRequestParamParser = new FilterRequestParamParser(converter);
        this.specificationBuilder = new SpecificationBuilder(new FilterModelBuilder());
    }

    public SpecificationMethodArgumentResolver() {
        var conversionService = new DefaultFormattingConversionService();
        this.filterRequestParamParser = new FilterRequestParamParser(conversionService::convert);
        this.specificationBuilder = new SpecificationBuilder(new FilterModelBuilder());
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
        var entityClass = ReflectionUtils.getGenericParameterType(parameter);

        var filterRequest = filterRequestParamParser.parse(webRequest);
        return specificationBuilder.build(entityClass, filterRequest);
    }
}
