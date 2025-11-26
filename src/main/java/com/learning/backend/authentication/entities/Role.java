package com.learning.backend.authentication.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.JoinColumn;
import lombok.Getter;
import lombok.Setter;

@Entity
public class Role extends BaseModel {
    @Getter
    @Setter
    private String roleName;

    @ManyToOne
    @JoinColumn(name = "userid")
    private User user;
}
