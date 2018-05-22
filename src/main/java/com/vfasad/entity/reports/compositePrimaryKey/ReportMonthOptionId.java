package com.vfasad.entity.reports.compositePrimaryKey;

import com.vfasad.service.OptionName;

import java.io.Serializable;
import java.util.Objects;

public class ReportMonthOptionId implements Serializable{
    private int year;
    private int month;
    private OptionName name;

    public ReportMonthOptionId() {
    }

    public ReportMonthOptionId(int year, int month, OptionName name) {
        this.year = year;
        this.month = month;
        this.name = name;
    }

    public int getYear() {
        return year;
    }

    public int getMonth() {
        return month;
    }

    public OptionName getName() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ReportMonthOptionId)) return false;
        ReportMonthOptionId that = (ReportMonthOptionId) o;
        return year == that.year &&
                month == that.month &&
                name == that.name;
    }

    @Override
    public int hashCode() {

        return Objects.hash(year, month, name);
    }
}



