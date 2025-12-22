package com.example.demo.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.entity.TimeSheet;

public interface TimeSheetRepository extends JpaRepository<TimeSheet, Long> {
    List<TimeSheet> findByEmployeeEmployeeId(Long employeeId);
    List<TimeSheet> findByEmployeeManagerEmployeeIdAndApprovedFalse(Long managerId);
    List<TimeSheet> findByEmployeeEmployeeIdAndDateBetween(
        Long employeeId, LocalDate start, LocalDate end
    );
      List<TimeSheet> findByEmployeeEmployeeIdAndDate(Long employeeId, LocalDate date);

}
