package com.example.demo.controller;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.entity.Notification;
import com.example.demo.service.EmployeeService;
import com.example.demo.service.NotificationService;
import com.example.demo.service.TimeSheetService;
import com.example.demo.service.EmailService;

import org.springframework.http.ResponseEntity;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

@RestController
@RequestMapping("/api/notifications")
@Tag(name = "Notifications")
public class NotificationController {

    private final NotificationService notificationService;
    private final TimeSheetService timeSheetService;
    private final EmployeeService employeeService;
    private final EmailService emailService;

    public NotificationController(
            NotificationService notificationService,
            TimeSheetService timeSheetService,
            EmployeeService employeeService,
            EmailService emailService) {
        this.notificationService = notificationService;
        this.timeSheetService = timeSheetService;
        this.employeeService = employeeService;
        this.emailService = emailService;
    }

    @GetMapping("/{managerId}")
    @Operation(summary = "Get unread notifications for a manager")
    public List<Notification> getNotifications(@PathVariable Long managerId) {
        return notificationService.getNotificationsForManager(managerId);
    }

 @PostMapping("/submit/{employeeId}")
@Operation(summary = "Submit monthly timesheet and notify manager")
public ResponseEntity<Map<String, Object>> submitMonthlyTimesheetAndNotify(
        @PathVariable Long employeeId,
        @RequestParam int month,
        @RequestParam int year) {

    String result = timeSheetService.submitMonthlyTimesheet(employeeId, month, year);

    // GET MANAGER OPTIONAL
    Optional<Long> maybeManagerId = employeeService.getManagerIdForEmployee(employeeId);

    // CASE 1 — NO MANAGER / TOP MANAGER
    if (maybeManagerId.isEmpty()) {
        // optional: create self notification
        notificationService.createNotification(
                employeeId,
                employeeId,
                "Timesheet submitted. No higher manager."
        );

        return ResponseEntity.ok(Map.of(
                "message", "Timesheet submitted by top manager.",
                "notification", "No manager found — skipped"
        ));
    }

    // CASE 2 — NORMAL EMPLOYEE OR MANAGER WITH HIGHER MANAGER
    Long managerId = maybeManagerId.get();
    String managerEmail = employeeService.getManagerEmail(managerId);
    String employeeName = employeeService.getEmployeeName(employeeId);

    notificationService.createNotification(
            managerId,
            employeeId,
            "Employee " + employeeName + " (ID: " + employeeId + ") submitted their timesheet."
    );

    String emailStatus = "sent";
    try {
        if (managerEmail == null || !managerEmail.contains("@")) {
            emailStatus = "skipped_invalid_email";
        } else {
            emailService.sendEmail(
                    managerEmail,
                    "Timesheet Submitted",
                    "Employee " + employeeName + " has submitted their timesheet for "
                            + month + "/" + year
            );
        }
    } catch (Exception ex) {
        emailStatus = "mail_error";
    }

    return ResponseEntity.ok(Map.of(
            "message", result,
            "notification", "Manager notified",
            "email", emailStatus
    ));
}


    @PutMapping("/{notificationId}/read")
    @Operation(summary = "Mark a notification as read")
    public ResponseEntity<Map<String, Object>> markNotificationAsRead(@PathVariable Long notificationId) {
        Notification notification = notificationService.getNotificationById(notificationId)
                .orElseThrow(() -> new RuntimeException("Notification not found with id: " + notificationId));
        notification.setRead(true);
        notificationService.save(notification);
        return ResponseEntity.ok(Map.of("message", "Notification marked as read"));
    }



    @GetMapping("/test-email")
public String testEmail() {
    emailService.sendEmail(
            "kajal.rajak7408@gmail.com",   // receiver
            "Test Email - Timesheet System",
            "Hello Kajal, this is a test email to check if mail sending is working!"
    );
    return "Test email sent!";
}

}
