package com.vfasad.entity.reports.compositePrimaryKey;

import java.io.Serializable;

public class ReportMonthId implements Serializable{
    private int year;
    private int month;

    public ReportMonthId() { }

    public ReportMonthId(int year, int month) {
        this.year = year;
        this.month = month;
    }

    public int getYear() {
        return year;
    }

    public int getMonth() {
        return month;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        if (getClass() != o.getClass()) return false;
        ReportMonthId other = (ReportMonthId) o;
        return year == other.year && month == other.month;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + year;
        result = prime * result + month;
        return result;
    }
}
