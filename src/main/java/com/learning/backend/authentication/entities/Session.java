package com.learning.backend.authentication.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Entity
@Setter
@Getter
@AllArgsConstructor
public class Session extends BaseModel{

    @Column(length = 512)
    private String sessionToken;
    private int userId;
    private String IPAddress;
    private String deviceInfo;
    private long expiresAt;
}
