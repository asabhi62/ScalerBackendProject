package com.learning.backend.authentication.entities;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import java.sql.Timestamp;

@MappedSuperclass
@AllArgsConstructor
@Setter
@Getter
@EnableJpaAuditing
public class BaseModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected int id;

    protected Timestamp createdAt;
    protected Timestamp updatedAt;

    public BaseModel() {
    }
}
