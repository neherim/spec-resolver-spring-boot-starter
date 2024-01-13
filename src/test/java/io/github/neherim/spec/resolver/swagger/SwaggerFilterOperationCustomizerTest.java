package io.github.neherim.spec.resolver.swagger;

import io.github.neherim.spec.resolver.BaseContextTest;
import io.github.neherim.spec.resolver.annotations.Filter;
import io.github.neherim.spec.resolver.filter.GreaterThan;
import io.github.neherim.spec.resolver.filter.In;
import io.github.neherim.spec.resolver.filter.LessThan;
import io.github.neherim.spec.resolver.filter.LikeIgnoreCase;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


public class SwaggerFilterOperationCustomizerTest extends BaseContextTest {
    public static final String SEARCH_URL = "/swagger-test";

    @Test
    void swaggerDocTest() throws Exception {
        mockMvc.perform(get("/v3/api-docs"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("paths./swagger-test.get.parameters[0].name").value("name"))
                .andExpect(jsonPath("paths./swagger-test.get.parameters[0].in").value("query"))
                .andExpect(jsonPath("paths./swagger-test.get.parameters[0].description").value("Employee's full name"))
                .andExpect(jsonPath("paths./swagger-test.get.parameters[0].schema.type").value("string"))
                .andExpect(jsonPath("paths./swagger-test.get.parameters[1].name").value("birthDateFrom"))
                .andExpect(jsonPath("paths./swagger-test.get.parameters[2].name").value("birthDateTo"))
                .andExpect(jsonPath("paths./swagger-test.get.parameters[3].name").value("role"))
                .andExpect(jsonPath("paths./swagger-test.get.parameters[3].schema.type").value("string"))
                .andExpect(jsonPath("paths./swagger-test.get.parameters[4]").doesNotExist());
    }

    @Entity
    public static class Employee {
        @Id
        private Long id;
        @Filter(filter = LikeIgnoreCase.class)
        @Schema(description = "Employee's full name")
        private String name;
        @Filter(name = "birthDateFrom", filter = GreaterThan.class)
        @Filter(name = "birthDateTo", filter = LessThan.class)
        private LocalDate birthDate;
        @Filter(filter = In.class, separator = ",")
        private Role role;

        public enum Role {
            DEVELOPER, QA, ARCHITECT, MANAGER
        }
    }

    public interface EmployeeRepository extends JpaSpecificationExecutor<Employee> {
    }

    @RestController
    public static class EmployeeController {
        @Autowired
        EmployeeRepository employeeRepository;

        @GetMapping(value = SEARCH_URL)
        public List<Employee> search(Specification<Employee> spec) {
            return employeeRepository.findAll(spec);
        }
    }
}
