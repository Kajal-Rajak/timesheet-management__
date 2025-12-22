package com.example.demo.service;

import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.example.demo.entity.Employee;
import com.example.demo.entity.User;
import com.example.demo.repository.EmployeeRepository;
import com.example.demo.repository.UserRepository;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final EmployeeRepository employeeRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthService(UserRepository userRepository, EmployeeRepository employeeRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.employeeRepository = employeeRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public User register(String username, String rawPassword, String name, String email, Long managerId) {
        if (userRepository.findByUsername(username).isPresent()) {
            throw new IllegalArgumentException("Username already exists");
        }

        Employee manager = null;
        if (managerId != null) {
            manager = employeeRepository.findById(managerId).orElse(null);
        }

        Employee employee = new Employee(name, email, manager);
        employee = employeeRepository.save(employee);

        // Store password as BCrypt hash
        User user = new User(username, passwordEncoder.encode(rawPassword), employee);
        return userRepository.save(user);
    }

    @Transactional(readOnly = true)
    public Optional<User> login(String username, String rawPassword) {
        Optional<User> userOpt = userRepository.findByUsername(username);

        // Verify using BCrypt
        return userOpt.filter(u -> passwordEncoder.matches(rawPassword, u.getPasswordHash()));
    }
}
