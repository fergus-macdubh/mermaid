package com.vfasad.dto;

import com.vfasad.entity.Product;
import lombok.Data;

@Data
public class StorageItem {
    private Long id;
    private int quantity;
    private String name;
    private Product.Unit unit;
    private String producer;
    private String supplier;
    private int averagePrice;
}
