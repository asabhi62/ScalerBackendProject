package com.learning.backend.authentication.controllers;

import com.learning.backend.authentication.models.UserInfo;
import com.learning.backend.authentication.services.AuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/auth")
public class AuthenticationController {

    @Autowired
    private AuthenticationService authenticationService;

    @PutMapping("/signup")
    public ResponseEntity<String> signup(@RequestBody UserInfo userInfo) {
        try {
            authenticationService.signup(userInfo);
            return ResponseEntity.status(HttpStatus.CREATED).body("Signup successful");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage()+"\nSignup failed");
        }
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody UserInfo userInfo, HttpServletResponse response) {
        try {
            String token = authenticationService.login(userInfo);
            response.setHeader("Authorization", "Bearer " + token);
            Cookie cookie = new Cookie("token", token);
            cookie.setHttpOnly(true);
            cookie.setPath("/");
            // Uncomment the next line if your app uses HTTPS
            // cookie.setSecure(true);
            response.addCookie(cookie);
            return ResponseEntity.ok("Login successful");
        } catch (SecurityException | IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Login failed");
        }
    }

    @GetMapping("/getUserDetails")
    public ResponseEntity<String> getUserDetails(HttpServletRequest request) {
        try {
            String token = null;
            Cookie[] cookies = request.getCookies();
            if (cookies != null) {
                for (Cookie cookie : cookies) {
                    if ("token".equals(cookie.getName())) {
                        token = cookie.getValue();
                        break;
                    }
                }
            }
            if (token == null || token.isEmpty()) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token not found in cookies");
            }
            String userDetails = authenticationService.getUserDetails(token);

            return ResponseEntity.ok(userDetails);
        } catch (SecurityException | IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to fetch user details");
        }
    }

}
