package com.vfasad.controller.reports;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@NoArgsConstructor
@AllArgsConstructor
public class PaintDto {
    long productId;
    String name;
    String producer;
    double sumQuantity;
    int orderCount;
    double sumClips;
    double sumSmallFurniture;
    double sumBigFurniture;
    double sumArea;

    public PaintDto(long productId, String name, String producer) {
        this.productId = productId;
        this.name = name;
        this.producer = producer;
    }
}
