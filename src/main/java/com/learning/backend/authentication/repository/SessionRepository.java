package com.learning.backend.authentication.repository;

import com.learning.backend.authentication.entities.Session;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SessionRepository extends JpaRepository<Session, Long> {

}
