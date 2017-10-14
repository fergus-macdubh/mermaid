package com.vfasad.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Set;

@Data
@Entity(name = "paint_order")
@NoArgsConstructor
public class Order {
    @Id
    @GeneratedValue(generator="optimized-sequence")
    private Long id;
    @ManyToOne
    private User manager;
    private int area;
    private String client;
    private double price;
    private LocalDateTime created = LocalDateTime.now();

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "order_fk")
    private Set<OrderConsume> consumes;

    @Enumerated(EnumType.STRING)
    private Status status = Status.CREATED;

    public enum Status {
        CREATED, IN_PROGRESS, SHIPPING, CLOSED, BLOCKED
    }

    public Order(User manager, int area, String client, double price, Set<OrderConsume> consumes) {
        this.manager = manager;
        this.area = area;
        this.client = client;
        this.price = price;
        this.consumes = consumes;
    }
}
