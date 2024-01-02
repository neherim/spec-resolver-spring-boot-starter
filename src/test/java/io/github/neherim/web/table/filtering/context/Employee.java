package io.github.neherim.web.table.filtering.context;

import io.github.neherim.web.table.filtering.annotations.Filter;
import io.github.neherim.web.table.filtering.filter.*;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Entity
@Table(name = "employee")
public class Employee {
    @Id
    @GeneratedValue
    private Long id;
    @Filter(filter = LikeIgnoreCase.class)
    private String name;
    @DateTimeFormat(pattern = "dd.MM.yyyy")
    @Filter(name = "birthDateFrom", filter = GreaterThan.class)
    @Filter(name = "birthDateTo", filter = LessThan.class)
    private LocalDate birthDate;
    @Filter(filter = Equal.class)
    private Integer salary;
    @Filter(filter = Equal.class)
    private boolean active;
    @Filter(filter = In.class, separator = ",")
    private Role role;

    public enum Role {
        DEVELOPER, QA, ARCHITECT, MANAGER
    }

    public Employee() {
    }

    public Employee(Long id, String name, LocalDate birthDate, Integer salary, boolean active, Role role) {
        this.id = id;
        this.name = name;
        this.birthDate = birthDate;
        this.salary = salary;
        this.active = active;
        this.role = role;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public Integer getSalary() {
        return salary;
    }

    public boolean isActive() {
        return active;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }

    public void setSalary(Integer salary) {
        this.salary = salary;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }
}
