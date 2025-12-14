package com.learning.backend.authentication.services;

import com.learning.backend.authentication.dto.ForgotPasswordRequestDto;
import com.learning.backend.authentication.dto.ResetPasswordRequestDto;
import com.learning.backend.authentication.entities.ResetPasswordToken;
import com.learning.backend.authentication.entities.Session;
import com.learning.backend.authentication.entities.SessionStatus;
import com.learning.backend.authentication.entities.User;
import com.learning.backend.authentication.repository.ResetPasswordTokenRepository;
import com.learning.backend.authentication.repository.SessionRepository;
import com.learning.backend.authentication.repository.UserRepository;
import com.learning.backend.authentication.utils.JwtTokenUtil;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Calendar;
import java.util.Date;
import java.util.Optional;
import java.util.UUID;

import static java.time.temporal.ChronoUnit.MONTHS;

@Service
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SessionRepository sessionRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private ResetPasswordTokenRepository resetPasswordTokenRepository;

    @Transactional
    public User registerUser(String email, String password, String name) {
        if (userRepository.findByEmail(email) != null) {
            throw new RuntimeException("User already exists");
        }
        User user = new User();
        user.setEmail(email);
        user.setName(name);
        user.setPassword(passwordEncoder.encode(password));
        return userRepository.save(user);
    }

    public Session authenticateUser(String email, String password) {
        User user = userRepository.findByEmail(email);

        if (user == null) {
            throw new RuntimeException("Invalid email or password");
        }

        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new RuntimeException("Invalid email or password");
        }

        Session session = new Session();
        session.setUser(user);
        session.setToken(JwtTokenUtil.generateToken(user.getId(), user.getEmail()));
        java.sql.Timestamp expiringAt = java.sql.Timestamp.valueOf(LocalDateTime.now().plus(1, MONTHS));
        session.setExpiringAt(expiringAt);
        session.setSessionStatus(SessionStatus.ACTIVE);

        return sessionRepository.save(session);
    }

    public void initiatePasswordReset(ForgotPasswordRequestDto requestDto) {
        User user = userRepository.findByEmail(requestDto.getEmail());
        if (user == null) {
            throw new RuntimeException("User not found");
        }

        // Check if token exists and delete it
        Optional<ResetPasswordToken> existingToken = resetPasswordTokenRepository.findAll().stream()
                .filter(t -> t.getUser().getId() == user.getId())
                .findFirst();
        existingToken.ifPresent(resetPasswordToken -> resetPasswordTokenRepository.delete(resetPasswordToken));

        ResetPasswordToken token = new ResetPasswordToken();
        token.setUser(user);
        token.setToken(UUID.randomUUID().toString());
        
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MINUTE, 30);
        token.setExpiryDate(calendar.getTime());

        resetPasswordTokenRepository.save(token);
        
        // In a real application, send email here
        System.out.println("Reset password token: " + token.getToken());
    }

    public Session completePasswordReset(ResetPasswordRequestDto requestDto) {
        ResetPasswordToken token = resetPasswordTokenRepository.findByToken(requestDto.getToken())
                .orElseThrow(() -> new RuntimeException("Invalid token"));

        if (token.getExpiryDate().before(new Date())) {
            throw new RuntimeException("Token expired");
        }

        User user = token.getUser();
        user.setPassword(passwordEncoder.encode(requestDto.getNewPassword()));
        userRepository.save(user);

        // Log the user in automatically
        Session session = new Session();
        session.setUser(user);
        session.setToken(JwtTokenUtil.generateToken(user.getId(), user.getEmail()));
        java.sql.Timestamp expiringAt = java.sql.Timestamp.valueOf(LocalDateTime.now().plus(1, MONTHS));
        session.setExpiringAt(expiringAt);
        session.setSessionStatus(SessionStatus.ACTIVE);
        
        return sessionRepository.save(session);
    }

    public void endSession(String token) {
        Optional<Session> sessionOptional = sessionRepository.findByTokenAndSessionStatus(token, SessionStatus.ACTIVE);

        if (sessionOptional.isEmpty()) {
            return;
        }

        Session session = sessionOptional.get();
        session.setSessionStatus(SessionStatus.ENDED);
        sessionRepository.save(session);
    }
}
