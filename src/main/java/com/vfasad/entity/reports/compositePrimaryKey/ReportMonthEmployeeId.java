package com.vfasad.entity.reports.compositePrimaryKey;

import java.io.Serializable;
import java.util.Objects;

public class ReportMonthEmployeeId implements Serializable{
    private int year;
    private int month;
    private Long employeeId;

    public ReportMonthEmployeeId() {
    }

    public ReportMonthEmployeeId(int year, int month, Long employeeId) {
        this.year = year;
        this.month = month;
        this.employeeId = employeeId;
    }

    public int getYear() {
        return year;
    }

    public int getMonth() {
        return month;
    }

    public Long getEmployee() {
        return employeeId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ReportMonthEmployeeId that = (ReportMonthEmployeeId) o;
        return year == that.year &&
                month == that.month &&
                Objects.equals(employeeId, that.employeeId);
    }

    @Override
    public int hashCode() {

        return Objects.hash(year, month, employeeId);
    }
}
