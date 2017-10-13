package com.vfasad.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Data
@Entity(name = "paint_order")
@NoArgsConstructor
public class Order {
    @Id
    @GeneratedValue(generator="optimized-sequence")
    private Long id;
    private String manager;
    private int area;
    private String client;
    private double price;
    private LocalDateTime created = LocalDateTime.now();

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "order_fk")
    private List<OrderConsume> consumes;

    @Enumerated(EnumType.STRING)
    private Status status = Status.CREATED;

    public enum Status {
        CREATED, IN_PROGRESS, SHIPPING, CLOSED, BLOCKED
    }

    public Order(String manager, int area, String client, double price, List<OrderConsume> consumes) {
        this.manager = manager;
        this.area = area;
        this.client = client;
        this.price = price;
        this.consumes = consumes;
    }
}