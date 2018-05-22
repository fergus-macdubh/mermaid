package com.vfasad.entity.reports;

import com.vfasad.entity.User;
import com.vfasad.entity.reports.compositePrimaryKey.ReportMonthEmployeeId;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "report_month_employee")
@IdClass(ReportMonthEmployeeId.class)
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
public class ReportMonthEmployee {
    @Id
    int year;
    @Id
    int month;
    @Id
    Long employeeId;
    String employeeName;
    String role;
    double area;
    double salary;
    double bonus;
    @ManyToOne
    User doneBy;
    LocalDateTime closed = LocalDateTime.now();

    public ReportMonthEmployee(int year, int month, Long employeeId) {
        this.year = year;
        this.month = month;
        this.employeeId = employeeId;
    }
}
