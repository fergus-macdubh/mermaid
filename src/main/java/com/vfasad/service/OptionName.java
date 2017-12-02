package com.vfasad.service;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum OptionName {
    SALARY_PAINTER("Зарплата маляра", "грн"),
    SALARY_LABORER("Зарплата помощника", "грн"),
    SALARY_MANAGER("Зарплата менеджера", "грн"),
    BONUS_PAINTER("Премия маляра (коэффициент)", ""),
    BONUS_LABORER("Премия помощника (коэффициент)", ""),
    BONUS_MANAGER("Премия менеджера (коэффициент)", "");

    private String desc;
    private String unit;
}
