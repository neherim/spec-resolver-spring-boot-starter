package io.github.neherim.web.table.filtering.context;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class EmployeeController {
    @Autowired
    private EmployeeRepository employeeRepository;

    @GetMapping("/employee/search")
    public List<Employee> search(Specification<Employee> spec) {
        return employeeRepository.findAll(spec);
    }
}
