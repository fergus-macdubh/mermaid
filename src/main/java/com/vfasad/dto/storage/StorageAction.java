package com.vfasad.dto.storage;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
public class StorageAction {
    @Id
    @GeneratedValue
    private Long id;

    private int quantity;
    private double price;
    private Type type;

    @ManyToOne
    private Item item;

//    @OneToMany
//    private Order order;

    public enum Type {
        PURCHASE, EXTRACT
    }
}
