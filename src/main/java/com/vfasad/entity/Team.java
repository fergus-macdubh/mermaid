package com.vfasad.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@Entity
@Table
@NoArgsConstructor
public class Team {
    @Id
    @GeneratedValue(generator = "optimized-sequence")
    private Long id;
    private String name;
}
