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

    private double quantity;
    private double price;

    @Enumerated(EnumType.STRING)
    private Type type;

    @ManyToOne
    private Product product;

    @ManyToOne
    private Order order;

    @ManyToOne
    private User actor;

    private LocalDateTime created = LocalDateTime.now();

    public static ProductAction createPurchaseAction(double quantity, double price, Product product, User user) {
        ProductAction pa = new ProductAction();
        pa.quantity = quantity;
        pa.price = price;
        pa.type = Type.PURCHASE;
        pa.product = product;
        pa.actor = user;
        return pa;
    }

    public static ProductAction createSpendAction(double quantity, Product product, User user, Order order) {
        ProductAction pa = new ProductAction();
        pa.quantity = quantity;
        pa.type = Type.SPEND;
        pa.product = product;
        pa.actor = user;
        pa.order = order;
        return pa;
    }

    public static ProductAction createReturnAction(double quantity, Product product, User user, Order order) {
        ProductAction pa = new ProductAction();
        pa.quantity = quantity;
        pa.type = Type.RETURN;
        pa.product = product;
        pa.actor = user;
        pa.order = order;
        return pa;
    }

    public static ProductAction createInventoryingAction(double quantity, Product product, User user) {
        ProductAction pa = new ProductAction();
        pa.quantity = quantity;
        pa.type = Type.INVENTORYING;
        pa.product = product;
        pa.actor = user;
        return pa;
    }
}
