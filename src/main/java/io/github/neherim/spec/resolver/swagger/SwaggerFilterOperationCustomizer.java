package io.github.neherim.spec.resolver.swagger;

import io.github.neherim.spec.resolver.annotations.Filter;
import io.github.neherim.spec.resolver.model.FilteredEntityParser;
import io.github.neherim.spec.resolver.utils.ReflectionUtils;
import io.swagger.v3.oas.models.Operation;
import io.swagger.v3.oas.models.media.Schema;
import io.swagger.v3.oas.models.media.StringSchema;
import io.swagger.v3.oas.models.parameters.Parameter;
import io.swagger.v3.oas.models.parameters.QueryParameter;
import org.apache.commons.lang3.StringUtils;
import org.springdoc.core.customizers.OperationCustomizer;
import org.springdoc.core.utils.SpringDocAnnotationsUtils;
import org.springdoc.core.utils.SpringDocUtils;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.web.method.HandlerMethod;

import java.lang.reflect.Field;

public class SwaggerFilterOperationCustomizer implements OperationCustomizer {

    public SwaggerFilterOperationCustomizer() {
        SpringDocUtils.getConfig().addRequestWrapperToIgnore(Specification.class);
    }

    @Override
    public Operation customize(Operation operation, HandlerMethod handlerMethod) {
        if (operation == null || handlerMethod == null) {
            return operation;
        }

        for (var methodParam : handlerMethod.getMethodParameters()) {
            if (methodParam.getParameterType().equals(Specification.class)) {
                var entityClass = ReflectionUtils.getGenericParameterType(methodParam);
                FilteredEntityParser.getAllFilteredFields(entityClass).stream()
                        .map(filteredField -> fromField(filteredField.annotation(), filteredField.field()))
                        .forEach(operation::addParametersItem);
            }
        }
        return operation;
    }

    private Parameter fromField(Filter annotation, Field field) {
        var filterParamName = StringUtils.isEmpty(annotation.name()) ? field.getName() : annotation.name();
        var schemaFromField = SpringDocAnnotationsUtils.resolveSchemaFromType(field.getType(), null, null,
                field.getAnnotations());
        var paramSchema = new Schema<>();
        paramSchema.setType(schemaFromField.getType());

        var param = new QueryParameter();
        param.setName(filterParamName);
        param.setDescription(schemaFromField.getDescription());

        if (StringUtils.isNotEmpty(annotation.separator())) {
            param.setSchema(new StringSchema());
        } else {
            param.setSchema(schemaFromField);
        }

        return param;
    }
}
