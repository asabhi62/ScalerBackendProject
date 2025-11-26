package com.learning.backend.authentication.services;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private AuthenticationService authenticationService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {
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
        if (token != null && !token.isEmpty()) {
            String userDetailsJson = authenticationService.getUserDetails(token);
            ObjectMapper mapper = new ObjectMapper();
            java.util.Map<String, Object> userDetailsMap = mapper.readValue(userDetailsJson, java.util.Map.class);
            String username = (String) userDetailsMap.get("email");
            String authoritiesStr = (String) userDetailsMap.get("authorities");
            java.util.List<org.springframework.security.core.authority.SimpleGrantedAuthority> authorities = new java.util.ArrayList<>();
            if (authoritiesStr != null && !authoritiesStr.isEmpty()) {
                for (String role : authoritiesStr.split(",")) {
                    authorities.add(new org.springframework.security.core.authority.SimpleGrantedAuthority("ROLE_" + role.trim()));
                }
            }
            if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                UsernamePasswordAuthenticationToken authToken =
                        new UsernamePasswordAuthenticationToken(username, null, authorities);
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }

        chain.doFilter(request, response);
    }
}
