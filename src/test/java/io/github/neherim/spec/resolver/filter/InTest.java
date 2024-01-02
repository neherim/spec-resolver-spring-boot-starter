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

public class InTest extends BaseContextTest {

    public static final String SEARCH_URL = "/in";
    @Autowired
    TestRepo testRepo;

    @BeforeEach
    void createEmployee() {
        testRepo.save(new TestEntity(1, LocalDate.of(2023, 10, 10), "string1", Color.GREEN));
        testRepo.save(new TestEntity(2, LocalDate.of(2023, 11, 11), "string2", Color.RED));
        testRepo.save(new TestEntity(3, LocalDate.of(2023, 12, 12), "string3", Color.BLUE));
    }

    @Test
    void filterWithRepeatedParamInRequest() throws Exception {
        mockMvc.perform(get(SEARCH_URL + "?id=2&id=3"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].id").value(2))
                .andExpect(jsonPath("$[1].id").value(3))
                .andExpect(jsonPath("$[2]").doesNotExist());
    }

    @Test
    void filterWithoutSeparator() throws Exception {
        mockMvc.perform(get(SEARCH_URL + "?str=string2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].str").value("string2"))
                .andExpect(jsonPath("$[1]").doesNotExist());
    }

    @Test
    void filterLocalDate() throws Exception {
        mockMvc.perform(get(SEARCH_URL + "?date=2023-11-11, 2023-10-10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].date").value("2023-10-10"))
                .andExpect(jsonPath("$[1].date").value("2023-11-11"))
                .andExpect(jsonPath("$[2]").doesNotExist());
    }

    @Test
    void filterString() throws Exception {
        mockMvc.perform(get(SEARCH_URL + "?str=string1|string2"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].str").value("string1"))
                .andExpect(jsonPath("$[1].str").value("string2"))
                .andExpect(jsonPath("$[2]").doesNotExist());
    }

    @Test
    void filterEnum() throws Exception {
        mockMvc.perform(get(SEARCH_URL + "?colorEnum=GREEN,BLUE"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].color").value("GREEN"))
                .andExpect(jsonPath("$[1].color").value("BLUE"))
                .andExpect(jsonPath("$[2]").doesNotExist());
    }

    @Entity
    public static class TestEntity {
        @Id
        @Filter(filter = In.class)
        public Integer id;
        @Filter(filter = In.class, separator = ", ")
        public LocalDate date;
        @Filter(filter = In.class, separator = "|")
        public String str;
        @Filter(name = "colorEnum", filter = In.class, separator = ",")
        public Color color;

        public TestEntity() {
        }

        public TestEntity(Integer id, LocalDate date, String str, Color color) {
            this.id = id;
            this.date = date;
            this.str = str;
            this.color = color;
        }
    }

    enum Color {
        RED, GREEN, BLUE
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
