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

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class LikeIgnoreCaseTest extends BaseContextTest {

    public static final String SEARCH_URL = "/like-ignore-case";
    @Autowired
    TestRepo testRepo;

    @BeforeEach
    void createEmployee() {
        testRepo.save(new TestEntity(1, "James"));
        testRepo.save(new TestEntity(2, "Edward"));
        testRepo.save(new TestEntity(3, "Joseph"));
        testRepo.save(new TestEntity(4, "Nicholas"));
    }

    @Test
    void findByFirstLetter() throws Exception {
        mockMvc.perform(get(SEARCH_URL + "?name=j"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].name").value("James"))
                .andExpect(jsonPath("$[1].name").value("Joseph"))
                .andExpect(jsonPath("$[2]").doesNotExist());
    }

    @Test
    void findByLastLetter() throws Exception {
        mockMvc.perform(get(SEARCH_URL + "?name=D"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].name").value("Edward"))
                .andExpect(jsonPath("$[1]").doesNotExist());
    }

    @Test
    void findByMiddleLetter() throws Exception {
        mockMvc.perform(get(SEARCH_URL + "?name=SeP"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].name").value("Joseph"))
                .andExpect(jsonPath("$[1]").doesNotExist());
    }

    @Entity
    public static class TestEntity {
        @Id
        public Integer id;
        @Filter(filter = LikeIgnoreCase.class)
        public String name;

        public TestEntity() {
        }

        public TestEntity(Integer id, String name) {
            this.id = id;
            this.name = name;
        }
    }

    @Repository
    public interface TestRepo extends JpaRepository<TestEntity, Integer>, JpaSpecificationExecutor<TestEntity> {
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
