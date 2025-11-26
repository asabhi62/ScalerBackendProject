package com.learning.backend.authentication.entities;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Entity
@Getter
@Setter
public class User extends BaseModel {
    private String username;
    private String password;
    private String email;
    @OneToMany(cascade = CascadeType.ALL)
    private List<Role> roles;
}
