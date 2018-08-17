package com.vfasad.entity.reports;

import com.vfasad.entity.User;
import com.vfasad.entity.reports.compositePrimaryKey.ReportMonthId;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "report_month_common")
@IdClass(ReportMonthId.class)
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
public class ReportMonthCommon {
    @Id
    int year;
    @Id
    int month;
    double totalExpenses;
    double electricity;
    double enterprenuerTax;
    double sumSalary;
    double sumBonuses;
    double buildingRent;
    double lineAmortization;
    double sumPriceConsumes;
    double otherExpenses;
    double sumArea;
    double totalIncome;
    double otherConsumes;
    double sumPrice;
    @ManyToOne
    User doneBy;
    LocalDateTime closed = LocalDateTime.now();

    public ReportMonthCommon(int year, int month) {
        this.year = year;
        this.month = month;
    }
}
