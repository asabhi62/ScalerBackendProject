package com.learning.backend.cart.entity;

import com.learning.backend.authentication.entities.BaseModel;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "cart_entries")
@Getter
@Setter
@NoArgsConstructor
public class CartEntry extends BaseModel {
    private String userId;
    private String productId;
    private String productName;
    private int quantity;
    private double price;
}
