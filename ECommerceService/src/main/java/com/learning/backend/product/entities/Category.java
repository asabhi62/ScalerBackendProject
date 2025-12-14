package com.learning.backend.product.entities;

import com.learning.backend.authentication.entities.BaseModel;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import java.util.List;

@Entity
@Getter
@Setter
public class Category extends BaseModel {

    @Column(nullable = false, unique = true, name = "category_name")
    private String name;

    @Column
    private String description;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "category", cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
    @Fetch(FetchMode.SELECT)
    private List<Product> products;
}
