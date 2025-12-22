package com.example.demo.dto;
import java.time.LocalDate;



public class TimeSheetResponse {

    private Long id;
    private Long employeeId;
    private LocalDate date;
    private double hoursWorked;
    private String description;
    private boolean approved;


public TimeSheetResponse(Long id, Long employeeId, LocalDate date,
                         double hoursWorked, String description, boolean approved) {
    this.id = id;
    this.employeeId = employeeId;
    this.date = date;
    this.hoursWorked = hoursWorked;
    this.description = description;
    this.approved = approved;
}

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(Long employeeId) {
        this.employeeId = employeeId;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public double getHoursWorked() {
        return hoursWorked;
    }

    public void setHoursWorked(double hoursWorked) {
        this.hoursWorked = hoursWorked;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isApproved() {
        return approved;
    }

    public void setApproved(boolean approved) {
        this.approved = approved;
    }
}
