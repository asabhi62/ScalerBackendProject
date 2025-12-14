package com.learning.backend.authentication.repository;

import com.learning.backend.authentication.entities.Session;
import com.learning.backend.authentication.entities.SessionStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface SessionRepository extends JpaRepository<Session, Integer> {
    Optional<Session> findByTokenAndSessionStatus(String token, SessionStatus sessionStatus);
}
