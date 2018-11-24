package com.vfasad.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "client")
public class Client {
    @Id
    @GeneratedValue(generator = "optimized-sequence")
    private Long id;
    private String name;
    private String phone;
    private String contact;
    private String email;
    @ManyToOne
    @JoinColumn(name = "manager_id")
    @NotNull
    private User manager;
    @Column(name = "deleted", nullable = false, columnDefinition = "boolean default false")
    private boolean deleted;
}
