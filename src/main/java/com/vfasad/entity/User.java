package com.vfasad.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity
@Table(name = "user_details", indexes = @Index(columnList = "email", unique = true))
@NoArgsConstructor
public class User {
    public static final String ROLE_ADMIN = "ROLE_ADMIN";
    public static final String ROLE_OPERATOR = "ROLE_OPERATOR";
    public static final String ROLE_PAINTER = "ROLE_PAINTER";
    public static final String ROLE_SALES = "ROLE_SALES";

    @Id
    @GeneratedValue(generator="optimized-sequence")
    private Long id;
    private String email;
    private String name;
    private String givenName;
    private String familyName;
    private String picture;
    private String gender;
    private String locale;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(name="user_authorities", joinColumns=@JoinColumn(name="user_id"))
    @Enumerated(EnumType.STRING)
    private List<String> authorities = new ArrayList<>();

    public User(String email, String name, String givenName, String familyName, String picture, String gender, String locale) {
        this.email = email;
        this.name = name;
        this.givenName = givenName;
        this.familyName = familyName;
        this.picture = picture;
        this.gender = gender;
        this.locale = locale;
    }
}
