package com.vfasad.entity.reports;

import com.vfasad.entity.reports.compositePrimaryKey.ReportMonthOptionId;
import com.vfasad.service.OptionName;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import javax.persistence.*;

@Data
@Entity
@IdClass(ReportMonthOptionId.class)
@Table(name = "report_month_option")
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
public class ReportMonthOption {
    @Id
    int year;
    @Id
    int month;
    @Id
    @Enumerated(EnumType.STRING)
    OptionName name;
    String value;

    public ReportMonthOption(int year, int month, OptionName name) {
        this.year = year;
        this.month = month;
        this.name = name;
    }
}
