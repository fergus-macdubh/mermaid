package com.vfasad.dto.storage;

import com.vfasad.dto.dictionary.Unit;
import lombok.Data;

import javax.persistence.*;

@Data
@Entity(name = "item")
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
}
