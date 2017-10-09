package com.vfasad.dto.storage;

import lombok.Data;

import javax.persistence.*;

@Data
@Entity
public class StorageAction {
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

    public enum Type {
        PURCHASE, EXTRACT
    }
}
