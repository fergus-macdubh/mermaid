package com.vfasad.service;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum OptionName {
    // reporting
    SALARY_PAINTER("Зарплата маляра", "грн"),
    SALARY_LABORER("Зарплата помощника", "грн"),
    SALARY_MANAGER("Зарплата начцеха", "грн"),
    BONUS_PAINTER("Премия маляра (коэффициент)", ""),
    BONUS_LABORER("Премия помощника (коэффициент)", ""),
    BONUS_MANAGER("Премия начцеха (коэффициент)", ""),
    ENTERPRENUER_TAX("Налог ФЛП", "грн"),
    ELECTRICITY("Расход электроэнергии (коэффициент)", ""),
    BUILDING_RENT("Аренда помещения", "грн"),
    LINE_AMORTIZATION("Амортизация линии", "грн"),
    OTHER_CONSUMES("Расходные материалы (коэффициент)", ""),
    OTHER_EXPENSES("Прочие расходы", "грн"),

    // new order form
    AUTO_CALC_PRICE_1("Авторасчет цены 1", "грн / м<sup>2</sup>"),
    AUTO_CALC_PRICE_2("Авторасчет цены 2", "грн / м<sup>2</sup>"),
    AUTO_CALC_PRICE_3("Авторасчет цены 3", "грн / м<sup>2</sup>"),
    AUTO_CALC_PRICE_4("Авторасчет цены 4", "грн / м<sup>2</sup>"),
    AUTO_CALC_PRICE_5("Авторасчет цены 5", "грн / м<sup>2</sup>"),

    CLIP_PRICE("Цена покраски кляймера", "грн / шт"),
    FURNITURE_SMALL_PRICE("Цена покраски мелкой фурнитуры", "грн / шт"),
    FURNITURE_BIG_PRICE("Цена покраски крупной фурнитуры", "грн / шт"),
    CLIP_TO_AREA("Пересчет кляймеров в м<sup>2</sup>", ""),
    FURNITURE_SMALL_TO_AREA("Пересчет мелкой фурнитуры в м<sup>2</sup>", ""),
    FURNITURE_BIG_TO_AREA("Пересчет крупной фурнитуры в м<sup>2</sup>", ""),

    PAINT_CONSUME("Расчетный расход краски", "кг / м<sup>2</sup>")
    ;

    private String desc;
    private String unit;
}
