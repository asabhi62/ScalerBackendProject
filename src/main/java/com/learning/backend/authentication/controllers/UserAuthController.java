package com.learning.backend.authentication.controllers;

import com.learning.backend.authentication.dto.ForgotPasswordRequestDto;
import com.learning.backend.authentication.dto.UserLoginRequestDto;
import com.learning.backend.authentication.dto.ResetPasswordRequestDto;
import com.learning.backend.authentication.entities.Session;
import com.learning.backend.authentication.services.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/auth")
public class UserAuthController {

    @Autowired
    private AuthService authService;

    @PostMapping("/authenticateUser")
    public ResponseEntity<Session> authenticateUser(@RequestBody UserLoginRequestDto loginRequest) {
        Session session = authService.authenticateUser(loginRequest.getEmail(), loginRequest.getPassword());
        return ResponseEntity.ok(session);
    }

    @PostMapping("/initiatePasswordReset")
    public ResponseEntity<Map<String, String>> initiatePasswordReset(@RequestBody ForgotPasswordRequestDto requestDto) {
        authService.initiatePasswordReset(requestDto);
        return ResponseEntity.ok(Map.of("message", "Password reset link sent to your email"));
    }

    @PostMapping("/completePasswordReset")
    public ResponseEntity<Session> completePasswordReset(@RequestBody ResetPasswordRequestDto requestDto) {
        Session session = authService.completePasswordReset(requestDto);
        return ResponseEntity.ok(session);
    }

    @DeleteMapping("/endSession/{token}")
    public ResponseEntity<Void> endSession(@PathVariable String token) {
        authService.endSession(token);
        return ResponseEntity.noContent().build();
    }
}
