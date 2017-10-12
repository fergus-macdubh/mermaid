package com.vfasad.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;

@Data
@Entity
@NoArgsConstructor
@Table(indexes = {
        @Index(columnList = "type"),
        @Index(columnList = "created")
})
public class ProductAction {
    public enum Type {
        PURCHASE, SPEND, INVENTORYING, RETURN
    }

    @Id
    @GeneratedValue(generator="optimized-sequence")
    private Long id;

    private int quantity;
    private double price;

    @Enumerated(EnumType.STRING)
    private Type type;

    @ManyToOne
    private Product product;

//    @OneToMany
//    private Order order;

    private String manager;
    private LocalDateTime created = LocalDateTime.now();

    public ProductAction(int quantity, double price, Type type, Product product, String manager) {
        this.quantity = quantity;
        this.price = price;
        this.type = type;
        this.product = product;
        this.manager = manager;
    }
}
