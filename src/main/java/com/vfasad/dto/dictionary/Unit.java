package com.vfasad.dto.dictionary;

import lombok.Getter;

@Getter
public enum Unit {
    KILOGRAM("кг"),
    ITEM("шт"),
    LITER("л");

    private String abbr;

    Unit(String abbr) {
        this.abbr = abbr;
    }
}
