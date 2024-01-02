package io.github.neherim.web.table.filtering;

import io.github.neherim.web.table.filtering.context.BaseContextTest;
import io.github.neherim.web.table.filtering.context.Employee;
import io.github.neherim.web.table.filtering.context.EmployeeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;

import static io.github.neherim.web.table.filtering.context.Employee.Role.DEVELOPER;
import static io.github.neherim.web.table.filtering.context.Employee.Role.QA;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@AutoConfigureMockMvc
public class WebTableFilteringTest extends BaseContextTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private EmployeeRepository employeeRepository;

    @BeforeEach
    void createEmployee() {
        employeeRepository.save(new Employee(null, "Иван", LocalDate.of(2000, 10, 1), 12000, true, QA));
        employeeRepository.save(new Employee(null, "Петр", LocalDate.of(2000, 1, 1), 12000, true, DEVELOPER));
    }

    @Test
    void testFindEmployee() throws Exception {
        System.out.println("df");
        mockMvc.perform(get("/v3/api-docs"))
                .andExpect(status().isOk())
                .andDo(print());
//        mockMvc.perform(get("/employee/search?firstName=Иван&birthDateFrom=2000-02-01&birthDateTo=2001-02-01"))
//                .andExpect(status().isOk())
//                .andDo(print());
    }
}
