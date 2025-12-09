package com.learning.backend.authentication.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Entity
@Getter
@Setter
public class ResetPasswordToken extends BaseModel {
    
    private String token;
    
    @OneToOne
    private User user;
    
    private Date expiryDate;
}
