package com.vfasad.entity.reports.compositePrimaryKey;

import com.vfasad.service.OptionName;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ReportMonthOptionId implements Serializable{
    private int year;
    private int month;
    private OptionName name;
}



