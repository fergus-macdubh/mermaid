package com.vfasad.entity;

import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

@Data
@Entity(name = "paint_order")
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Order {
    public enum Status {
        CREATED, IN_PROGRESS, SHIPPING, CLOSED, BLOCKED, CANCELLED
    }

    @Id
    @GeneratedValue(generator = "optimized-sequence")
    Long id;
    int clipCount;
    int furnitureSmallCount;
    int furnitureBigCount;
    double area;
    @Column(name = "document_column")
    String document;
    double price;
    LocalDateTime created = LocalDateTime.now();
    LocalDate planned;
    LocalDateTime completed;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "order_fk")
    private Set<OrderConsume> consumes;

    @Enumerated(EnumType.STRING)
    private Status status = Status.CREATED;

    @ManyToOne
    @JoinColumn(name = "team_fk")
    private Team team;

    @ManyToOne
    @JoinColumn(name = "client_id")
    private Client client;

    @ManyToMany(fetch = FetchType.EAGER)
    private List<User> doneBy;

    public Order(double area, int clipCount, int furnitureSmallCount, int furnitureBigCount,
                 String document, double price, Set<OrderConsume> consumes, LocalDate planned, Client client) {
        this.area = area;
        this.document = document;
        this.price = price;
        this.consumes = consumes;
        this.planned = planned;
        this.clipCount = clipCount;
        this.furnitureSmallCount = furnitureSmallCount;
        this.furnitureBigCount = furnitureBigCount;
        this.client = client;
    }
}
