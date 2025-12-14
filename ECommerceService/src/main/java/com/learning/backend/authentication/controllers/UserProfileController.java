package com.learning.backend.authentication.controllers;

import com.learning.backend.authentication.dto.UserRegistrationRequestDto;
import com.learning.backend.authentication.services.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/profile")
public class UserProfileController {

    @Autowired
    private AuthService authService;

    @PostMapping("/registerUser")
    public ResponseEntity<Void> registerUser(@RequestBody UserRegistrationRequestDto signUpRequest) {
        authService.registerUser(signUpRequest.getEmail(), signUpRequest.getPassword(), signUpRequest.getName());
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
