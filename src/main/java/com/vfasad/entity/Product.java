package com.vfasad.entity;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@Entity
@NoArgsConstructor
public class Product {
    @Id
    @GeneratedValue(generator="optimized-sequence")
    private Long id;
    private double quantity;
    private String name;

    @Enumerated(EnumType.STRING)
    private Unit unit;
    private String producer;
    private String supplier;
    private double price;

    public Product(String name, Unit unit, String producer, String supplier) {
        this.name = name;
        this.unit = unit;
        this.producer = producer;
        this.supplier = supplier;
    }

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
}
