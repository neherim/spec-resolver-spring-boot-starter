package io.github.neherim.web.table.filtering.context;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.neherim.web.table.filtering.SpecificationMethodArgumentResolver;
import io.github.neherim.web.table.filtering.builder.EntityClassParser;
import io.github.neherim.web.table.filtering.builder.SpecificationBuilder;
import io.github.neherim.web.table.filtering.request.FilterRequestParser;
import io.github.neherim.web.table.filtering.request.RequestBodyParser;
import io.github.neherim.web.table.filtering.request.RequestParamsParser;
import io.github.neherim.web.table.filtering.request.converter.SpringConverter;
import io.github.neherim.web.table.filtering.swagger.FilterOperationCustomizer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.format.support.DefaultFormattingConversionService;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@EnableJpaRepositories
@SpringBootApplication
public class ApplicationTestContext {

    public static void main(String[] args) {
        SpringApplication.run(ApplicationTestContext.class, args);
    }

    @Configuration
    public class WebConfig implements WebMvcConfigurer {
        @Autowired
        private ObjectMapper objectMapper;

        @Bean
        public FilterOperationCustomizer filterOperationCustomizer() {
            return new FilterOperationCustomizer(new EntityClassParser());
        }

        @Override
        public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
            var specificationArgResolver = new SpecificationMethodArgumentResolver(
                    new FilterRequestParser(
                            new SpringConverter(new DefaultFormattingConversionService()),
                            new RequestBodyParser(objectMapper),
                            new RequestParamsParser()
                    ),
                    new SpecificationBuilder(new EntityClassParser()));
            resolvers.add(specificationArgResolver);
        }
    }
}
