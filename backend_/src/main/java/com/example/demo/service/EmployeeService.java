

package com.example.demo.service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.dto.HierarchyResponse;
import com.example.demo.dto.PersonDto;
import com.example.demo.entity.Employee;
import com.example.demo.repository.EmployeeRepository;

@Service
public class EmployeeService {

    private final EmployeeRepository employeeRepository;

    public EmployeeService(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    @Transactional(readOnly = true)
    public java.util.Optional<Long> getManagerIdForEmployee(Long employeeId) {
        return employeeRepository.findByIdWithManager(employeeId)
                .map(Employee::getManager)
                .map(Employee::getEmployeeId);
    }

    @Transactional(readOnly = true)
    public Optional<HierarchyResponse> getHierarchy(Long employeeId) {
        Optional<Employee> empOpt = employeeRepository.findByIdWithManager(employeeId);
        return empOpt.map(employee -> {
            PersonDto employeeDto = toDto(employee);
            PersonDto managerDto = employee.getManager() != null ? toDto(employee.getManager()) : null;
            List<PersonDto> subs = employeeRepository.findByManagerEmployeeId(employee.getEmployeeId())
                    .stream().map(this::toDto).collect(Collectors.toList());
            return new HierarchyResponse(employeeDto, managerDto, subs);
        });
    }

    private PersonDto toDto(Employee e) {
        return new PersonDto(e.getEmployeeId(), e.getName(), e.getEmail());
    }

    @Transactional
    public Optional<PersonDto> updateEmployee(Long employeeId, String name, String email) {
        return employeeRepository.findById(employeeId).map(emp -> {
            emp.setName(name);
            emp.setEmail(email);
            Employee saved = employeeRepository.save(emp);
            return toDto(saved);
        });
    }

     @Transactional
public void deleteEmployee(Long id) {
    Employee emp = employeeRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Employee not found with id " + id));

    // Remove links from subordinates before deleting (important!)
    List<Employee> subs = employeeRepository.findByManagerEmployeeId(id);
    for (Employee sub : subs) {
        sub.setManager(null);
        employeeRepository.save(sub);
    }

    employeeRepository.delete(emp);
}

public String getManagerEmail(Long managerId) {
    return employeeRepository.findById(managerId)
            .map(Employee::getEmail)
            .orElseThrow(() -> new RuntimeException("Manager email not found"));
}

public String getEmployeeName(Long employeeId) {
    return employeeRepository.findById(employeeId)
            .map(Employee::getName)
            .orElseThrow(() -> new RuntimeException("Employee name not found"));
}

}