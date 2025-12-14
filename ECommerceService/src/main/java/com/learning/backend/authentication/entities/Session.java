package com.learning.backend.authentication.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Timestamp;
import java.util.Date;

@Entity
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Session extends BaseModel {

    @Column(length = 512)
    private String token;
    
    @ManyToOne
    private User user;
    
    @Column(name = "expires_at")
    private Timestamp expiringAt;
    
    @Enumerated(EnumType.ORDINAL)
    private SessionStatus sessionStatus;
}
