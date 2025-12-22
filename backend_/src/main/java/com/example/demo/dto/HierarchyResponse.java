package com.example.demo.dto;

import java.util.List;

public class HierarchyResponse {
    private PersonDto employee;
    private PersonDto manager;
    private List<PersonDto> subordinates;

    public HierarchyResponse() {}

    public HierarchyResponse(PersonDto employee, PersonDto manager, List<PersonDto> subordinates) {
        this.employee = employee;
        this.manager = manager;
        this.subordinates = subordinates;
    }

    public PersonDto getEmployee() { return employee; }
    public void setEmployee(PersonDto employee) { this.employee = employee; }
    public PersonDto getManager() { return manager; }
    public void setManager(PersonDto manager) { this.manager = manager; }
    public List<PersonDto> getSubordinates() { return subordinates; }
    public void setSubordinates(List<PersonDto> subordinates) { this.subordinates = subordinates; }
}