package com.vfasad.dto.storage;

import com.vfasad.dto.dictionary.Unit;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@Entity(name = "item")
@NoArgsConstructor
public class Item {
    @Id
    @GeneratedValue(generator="optimized-sequence")
    private Long id;
    private int quantity;
    private String name;

    @Enumerated(EnumType.STRING)
    private Unit unit;
    private String producer;
    private String supplier;

    public Item(String name, Unit unit, String producer, String supplier) {
        this.name = name;
        this.unit = unit;
        this.producer = producer;
        this.supplier = supplier;
    }
}
