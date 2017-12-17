package com.vfasad.controller.reports;

import java.time.Month;
import java.time.format.TextStyle;
import java.util.Locale;

public abstract class AbstractReportController {
    protected String getMonthName(int month) {
        return Month.of(month)
                .getDisplayName(
                        TextStyle.FULL_STANDALONE,
                        new Locale("ru", "UA"));
    }
}
