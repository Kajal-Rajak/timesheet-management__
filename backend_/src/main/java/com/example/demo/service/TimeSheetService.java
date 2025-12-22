package com.example.demo.service;

import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.entity.Employee;
import com.example.demo.entity.TimeSheet;
import com.example.demo.repository.EmployeeRepository;
import com.example.demo.repository.TimeSheetRepository;

@Service
public class TimeSheetService {

    private final TimeSheetRepository timeSheetRepository;
    private final EmployeeRepository employeeRepository;
    private final NotificationService notificationService;

    public TimeSheetService(TimeSheetRepository timeSheetRepository, EmployeeRepository employeeRepository, NotificationService notificationService) {
        this.timeSheetRepository = timeSheetRepository;
        this.employeeRepository = employeeRepository;
        this.notificationService = notificationService;
    }

    @Transactional
    public TimeSheet submitTimesheet(Long employeeId, LocalDate date, double hours, String description) {
        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new RuntimeException("Employee not found"));
        TimeSheet ts = new TimeSheet(employee, date, hours, description);
            TimeSheet saved = timeSheetRepository.save(ts);
            return saved;
    }

    @Transactional(readOnly = true)
    public List<TimeSheet> getEmployeeTimesheets(Long employeeId) {
        return timeSheetRepository.findByEmployeeEmployeeId(employeeId);
    }

    @Transactional(readOnly = true)
    public List<TimeSheet> getPendingApprovals(Long managerId) {
        return timeSheetRepository.findByEmployeeManagerEmployeeIdAndApprovedFalse(managerId);
    }

    @Transactional
    public TimeSheet approveTimesheet(Long timesheetId) {
        TimeSheet ts = timeSheetRepository.findById(timesheetId)
                .orElseThrow(() -> new RuntimeException("Timesheet not found"));
        ts.setApproved(true);
        return timeSheetRepository.save(ts);
    }


  public List<TimeSheet> getTimesheetsByEmployeeAndMonthYear(Long employeeId, int month, int year) {
    YearMonth yearMonth = YearMonth.of(year, month);
    LocalDate startDate = yearMonth.atDay(1);
    LocalDate endDate = yearMonth.atEndOfMonth();
    return timeSheetRepository.findByEmployeeEmployeeIdAndDateBetween(employeeId, startDate, endDate);
}

public List<TimeSheet> approveTimesheetsByEmployeeAndMonth(Long employeeId, int month, int year) {
    List<TimeSheet> timesheets = getTimesheetsByEmployeeAndMonthYear(employeeId, month, year);
    
    for (TimeSheet ts : timesheets) {
        ts.setApproved(true); // Assuming you have an 'approved' field
        // Optionally, you can set approvedBy and approvedDate
    }
    
    return timeSheetRepository.saveAll(timesheets); // Save all updated timesheets
}

public List<TimeSheet> setApprovalByEmployeeAndDay(Long employeeId, int day, int month, int year, boolean approve) {
    LocalDate date = LocalDate.of(year, month, day);
    
    // Fetch timesheets for the employee and date
    List<TimeSheet> timesheets = timeSheetRepository.findByEmployeeEmployeeIdAndDate(employeeId, date);
    
    for (TimeSheet ts : timesheets) {
        ts.setApproved(approve); // true = approve, false = reject/unapprove
    }
    
    return timeSheetRepository.saveAll(timesheets);
}


public TimeSheet setApprovalById(Long id, boolean approve) {
    TimeSheet ts = timeSheetRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Timesheet not found"));

    ts.setApproved(approve);
    return timeSheetRepository.save(ts);
}

 @Transactional
public String submitMonthlyTimesheet(Long employeeId, int month, int year) {

    List<TimeSheet> sheets = getTimesheetsByEmployeeAndMonthYear(employeeId, month, year);

    if (sheets.isEmpty()) {
        return "No timesheets found for this month";
    }

    timeSheetRepository.saveAll(sheets);

    // Get employee and manager
    Employee emp = employeeRepository.findById(employeeId)
            .orElseThrow(() -> new RuntimeException("Employee not found"));

    Long managerId = emp.getManager().getEmployeeId();

    return "Monthly timesheet submitted successfully.";
}


}
