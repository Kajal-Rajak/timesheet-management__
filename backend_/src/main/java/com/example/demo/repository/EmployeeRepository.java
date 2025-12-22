package com.example.demo.repository;


import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.demo.entity.Employee;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {

    @Query("SELECT e FROM Employee e LEFT JOIN FETCH e.manager WHERE e.employeeId = :id")
    Optional<Employee> findByIdWithManager(@Param("id") Long id);

    List<Employee> findByManagerEmployeeId(Long managerId);
}