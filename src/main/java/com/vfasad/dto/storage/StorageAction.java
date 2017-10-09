package com.vfasad.dto.storage;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
public class StorageAction {

    public enum Type {
        PURCHASE, EXTRACT
    }
    @Id
    @GeneratedValue(generator="optimized-sequence")
    private Long id;

    private int quantity;
    private double price;

    @Enumerated(EnumType.STRING)
    private Type type;

    @ManyToOne
    private Item item;

//    @OneToMany
//    private Order order;

    private String manager;

    public StorageAction(int quantity, double price, Type type, Item item, String manager) {
        this.quantity = quantity;
        this.price = price;
        this.type = type;
        this.item = item;
        this.manager = manager;
    }
}
