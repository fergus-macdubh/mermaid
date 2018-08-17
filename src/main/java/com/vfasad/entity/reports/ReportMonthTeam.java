package com.vfasad.entity.reports;

import com.vfasad.entity.User;
import com.vfasad.entity.reports.compositePrimaryKey.ReportMonthTeamId;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "report_month_team")
@IdClass(ReportMonthTeamId.class)
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
public class ReportMonthTeam {
    @Id
    int year;
    @Id
    int month;
    @Id
    Long teamId;
    String teamName;
    double area;
    double income;
    double paintExpenses;
    double paintPrice;
    double salary;
    double bonus;
    @ManyToOne
    User doneBy;
    LocalDateTime closed = LocalDateTime.now();

    public ReportMonthTeam(int year, int month, Long teamId) {
        this.year = year;
        this.month = month;
        this.teamId = teamId;
    }
}
