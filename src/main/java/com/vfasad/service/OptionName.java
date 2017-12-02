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
    BONUS_MANAGER("Премия менеджера (коэффициент)", ""),
    ENTERPRENUER_TAX("Налог ФЛП", "грн"),
    ELECTRICITY("Расход электроэнергии (коэффициент)", ""),
    BUILDING_RENT("Аренда помещения", "грн"),
    LINE_AMORTIZATION("Амортизация линии", "грн"),
    OTHER_CONSUMES("Расходные материалы (коэффициент)", ""),
    OTHER_EXPENSES("Прочие расходы", "грн"),
    ;

    private String desc;
    private String unit;
}
