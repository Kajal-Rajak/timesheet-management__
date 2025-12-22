package com.example.demo.controller;

import java.util.Collections;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import jakarta.validation.Valid;

import com.example.demo.dto.TimeSheetRequest;
import com.example.demo.dto.TimeSheetResponse;
import com.example.demo.entity.TimeSheet;
import com.example.demo.service.TimeSheetService;

@RestController
@RequestMapping("/api/timesheets")
public class TimeSheetController {

    private final TimeSheetService timeSheetService;

    public TimeSheetController(TimeSheetService timeSheetService) {
        this.timeSheetService = timeSheetService;
    }

@PostMapping("/submit")
public TimeSheet submitTimesheet(@Valid @RequestBody TimeSheetRequest request) {
    return timeSheetService.submitTimesheet(
            request.getEmployeeId(),
            request.getDate(),
            request.getHoursWorked(),
            request.getDescription()
    );
}



    @GetMapping("/employee/{employeeId}")
    public List<TimeSheet> getEmployeeTimesheets(@PathVariable Long employeeId) {
        return timeSheetService.getEmployeeTimesheets(employeeId);
    }

    @GetMapping("/pending/{managerId}")
    public List<TimeSheet> getPendingApprovals(@PathVariable Long managerId) {
        return timeSheetService.getPendingApprovals(managerId);
    }

    @PutMapping("/approve/{timesheetId}")
    public TimeSheet approveTimesheet(@PathVariable Long timesheetId) {
        return timeSheetService.approveTimesheet(timesheetId);
    }

   @GetMapping("/employee/{employeeId}/filter")
public ResponseEntity<List<TimeSheetResponse>> getEmployeeTimesheetsByMonthAndYear(
        @PathVariable Long employeeId,
        @RequestParam int month,
        @RequestParam int year) {

    List<TimeSheet> timesheets =
            timeSheetService.getTimesheetsByEmployeeAndMonthYear(employeeId, month, year);

    if (timesheets.isEmpty()) {
        return ResponseEntity.ok(Collections.emptyList());
    }

    List<TimeSheetResponse> dtoList = timesheets.stream().map(t ->
            new TimeSheetResponse(
                    t.getId(),
                    t.getEmployee().getEmployeeId(),
                    t.getDate(),
                    t.getHoursWorked(),
                    t.getDescription(),
                    t.isApproved()
            )
    ).toList();

    return ResponseEntity.ok(dtoList);
}


@PutMapping("/approve")
public List<TimeSheet> approveTimesheetsByEmployeeAndMonth(
        @RequestParam Long employeeId,
        @RequestParam int month,
        @RequestParam int year) {
    return timeSheetService.approveTimesheetsByEmployeeAndMonth(employeeId, month, year);
}

// Get timesheets for an employee filtered by month/year

// Approve timesheet(s) by employee for a specific day
@PutMapping("/approve/day")
public List<TimeSheet> approveTimesheetsByEmployeeAndDay(
        @RequestParam Long employeeId,
        @RequestParam int day,
        @RequestParam int month,
        @RequestParam int year,
        @RequestParam boolean approve) { // true = approve, false = reject/unapprove
    return timeSheetService.setApprovalByEmployeeAndDay(employeeId, day, month, year, approve);
}
@PutMapping("/set/{timesheetId}")
public ResponseEntity<TimeSheet> setApprovalById(
        @PathVariable Long timesheetId,
        @RequestParam boolean approve) {

    TimeSheet updated = timeSheetService.setApprovalById(timesheetId, approve);
    return ResponseEntity.ok(updated);
}



}
