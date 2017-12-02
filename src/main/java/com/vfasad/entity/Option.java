package com.vfasad.entity;

import com.vfasad.service.OptionName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;

@Data
@Entity(name = "option")
@NoArgsConstructor
@AllArgsConstructor
public class Option {
    @Id
    @Enumerated(EnumType.STRING)
    private OptionName name;

    private String value;

    public Option(OptionName name) {
        this.name = name;
    }
}
