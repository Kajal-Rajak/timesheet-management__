package com.example.demo.controller;



import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.dto.HierarchyResponse;
import com.example.demo.service.EmployeeService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;



@RestController
@RequestMapping("/api/employees")
@CrossOrigin(origins = {"http://localhost:4200", "http://localhost:8000"})
public class EmployeeController {

    private final EmployeeService employeeService;

    public EmployeeController(EmployeeService employeeService) {
        this.employeeService = employeeService;
    }

    @GetMapping("/hierarchy/{id}")
    public ResponseEntity<?> getHierarchy(@PathVariable("id") Long id) {
        Optional<HierarchyResponse> res = employeeService.getHierarchy(id);
        return res.<ResponseEntity<?>>map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

@Operation(summary = "Delete an employee by ID")
@DeleteMapping("/{id}")
public ResponseEntity<?> deleteEmployee(
        @Parameter(description = "ID of the employee to delete")
        @PathVariable("id") Long id) {
    try {
        employeeService.deleteEmployee(id);
        return ResponseEntity.noContent().build();
    } catch (RuntimeException e) {
        return ResponseEntity.notFound().build();
    }
}


}