package io.github.neherim.web.table.filtering.swagger;

import io.github.neherim.web.table.filtering.builder.EntityClassParser;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.Operation;
import io.swagger.v3.oas.models.media.Schema;
import io.swagger.v3.oas.models.parameters.Parameter;
import org.springdoc.core.customizers.OperationCustomizer;
import org.springdoc.core.utils.SpringDocAnnotationsUtils;
import org.springframework.core.MethodParameter;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.web.method.HandlerMethod;

import java.lang.reflect.ParameterizedType;

public class FilterOperationCustomizer implements OperationCustomizer {
    private final EntityClassParser entityClassParser;

    public FilterOperationCustomizer(EntityClassParser entityClassParser) {
        this.entityClassParser = entityClassParser;
    }

    @Override
    public Operation customize(Operation operation, HandlerMethod handlerMethod) {
        if (operation == null || handlerMethod == null) {
            return operation;
        }

        for (var param : handlerMethod.getMethodParameters()) {
            if (param.getParameterType().equals(Specification.class)) {
                var entityClass = getEntityType(param);
                var parsedClass = entityClassParser.parseClass(entityClass);
                for (var filteredField : parsedClass) {
                    var parameter = new Parameter();
                    parameter.setName(filteredField.filterParamName());
                    SpringDocAnnotationsUtils.resolveSchemaFromType()

                    var schema = SpringDocAnnotationsUtils.resolveSchemaFromType(domainType,
                            null, null, param.getParameterAnnotations());

                    parameter.schema(schema);
                    operation.addParametersItem(parameter);
                }
                System.out.println("sdf");
            }
        }
        return operation;
    }

    private Class<?> getEntityType(MethodParameter parameter) {
        return (Class<?>) ((ParameterizedType) parameter.getGenericParameterType()).getActualTypeArguments()[0];
    }
}
