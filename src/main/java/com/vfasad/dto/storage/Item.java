package com.vfasad.dto.storage;

import com.vfasad.dto.dictionary.Unit;
import lombok.Data;

import javax.persistence.*;

@Data
@Entity(name = "item")
public class Item {
    @Id
    @GeneratedValue
    private Long id;
    private int quantity;
    private Double price;
    private String name;
    private Unit unit;
    private String producer;
    private String supplier;
}
