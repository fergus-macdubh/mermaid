package com.vfasad.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Data
@Entity(name = "paint_order_consume")
@NoArgsConstructor
public class OrderConsume {
    @Id
    @GeneratedValue(generator="optimized-sequence")
    private Long id;
    @ManyToOne
    private Product product;
    private int calculatedQuantity;
    private int actualUsedQuantity;

    public OrderConsume(Product product, int calculatedQuantity) {
        this.product = product;
        this.calculatedQuantity = calculatedQuantity;
    }
}
