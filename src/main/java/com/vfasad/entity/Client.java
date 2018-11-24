package com.vfasad.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity(name = "client")
public class Client {
    @Id
    private Long id;
    private String name;
    private String phone;
    private String contact;
    private String email;
    @ManyToOne
    @JoinColumn(name = "manager_id")
    private User manager;
}
