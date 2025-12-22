package com.example.demo.dto;

import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public class TimeSheetRequest {

    @NotNull(message = "Employee ID is required")
    private Long employeeId;

    @JsonFormat(pattern = "yyyy-MM-dd")  // REQUIRED so Spring does NOT store null
    @NotNull(message = "Date is required")
    private LocalDate date;

    @Positive(message = "Hours worked must be positive")
    private double hoursWorked;
    private String description;

    // Getters and setters
    public Long getEmployeeId() { return employeeId; }
    public void setEmployeeId(Long employeeId) { this.employeeId = employeeId; }

    public LocalDate getDate() { return date; }
    public void setDate(LocalDate date) { this.date = date; }

    public double getHoursWorked() { return hoursWorked; }
    public void setHoursWorked(double hoursWorked) { this.hoursWorked = hoursWorked; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
}
