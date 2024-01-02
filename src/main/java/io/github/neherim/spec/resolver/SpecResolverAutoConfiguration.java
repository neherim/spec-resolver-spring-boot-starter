package io.github.neherim.spec.resolver;

import io.github.neherim.spec.resolver.swagger.SwaggerFilterOperationCustomizer;
import org.springdoc.core.configuration.SpringDocConfiguration;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@AutoConfiguration
@ConditionalOnWebApplication
public class SpecResolverAutoConfiguration {

    @Bean
    @ConditionalOnClass(SpringDocConfiguration.class)
    public SwaggerFilterOperationCustomizer filterOperationCustomizer() {
        return new SwaggerFilterOperationCustomizer();
    }

    @Bean
    @ConditionalOnMissingBean
    public SpecificationMethodArgumentResolver specificationResolver() {
        return new SpecificationMethodArgumentResolver();
    }

    @Bean
    public WebMvcConfigurer specArgResolverWebConfig(SpecificationMethodArgumentResolver specificationResolver) {
        return new WebMvcConfigurer() {
            @Override
            public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
                resolvers.add(specificationResolver);
            }
        };
    }
}
