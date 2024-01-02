package io.github.neherim.spec.resolver.filter;

import io.github.neherim.spec.resolver.BaseContextTest;
import io.github.neherim.spec.resolver.annotations.Filter;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class GreaterThanTest extends BaseContextTest {

    public static final String SEARCH_URL = "/greater-than";
    @Autowired
    TestRepo testRepo;

    @BeforeEach
    void createEmployee() {
        testRepo.save(new TestEntity(1, LocalDate.of(2023, 10, 10)));
        testRepo.save(new TestEntity(2, LocalDate.of(2023, 11, 11)));
        testRepo.save(new TestEntity(3, LocalDate.of(2023, 12, 12)));
    }

    @Test
    void findByIntField() throws Exception {
        mockMvc.perform(get(SEARCH_URL + "?idFrom=1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].id").value(2))
                .andExpect(jsonPath("$[1].id").value(3))
                .andExpect(jsonPath("$[2]").doesNotExist());
    }

    @Test
    void findByDateField() throws Exception {
        mockMvc.perform(get(SEARCH_URL + "?dateFrom=2023-11-11"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].date").value("2023-12-12"))
                .andExpect(jsonPath("$[1]").doesNotExist());
    }

    @Entity
    public static class TestEntity {
        @Id
        @Filter(name = "idFrom", filter = GreaterThan.class)
        public Integer id;
        @Filter(name = "dateFrom", filter = GreaterThan.class)
        public LocalDate date;

        public TestEntity() {
        }

        public TestEntity(Integer id, LocalDate date) {
            this.id = id;
            this.date = date;
        }
    }

    @Repository
    public interface TestRepo extends JpaRepository<TestEntity, Long>, JpaSpecificationExecutor<TestEntity> {
    }

    @RestController
    public static class TestController {
        @Autowired
        TestRepo testRepo;

        @GetMapping(value = SEARCH_URL)
        public List<TestEntity> search(Specification<TestEntity> spec) {
            return testRepo.findAll(spec);
        }
    }

}
