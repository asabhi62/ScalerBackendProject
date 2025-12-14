package com.learning.backend.authentication.services;

import com.learning.backend.authentication.dto.CustomGrantedAuthority;
import com.learning.backend.authentication.dto.CustomUserDetails;
import com.learning.backend.authentication.entities.Role;
import com.learning.backend.authentication.entities.User;
import com.learning.backend.authentication.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    @Transactional(readOnly = true)
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(username);
        CustomUserDetails customUserDetails;
        if(user!=null) {
            customUserDetails = new CustomUserDetails();
            customUserDetails.setEmail(user.getEmail());
            customUserDetails.setPassword(user.getPassword());
            // Set roles/authorities if needed
            List<Role> roles = user.getRoles();
            List<CustomGrantedAuthority> authorities = roles.stream()
                    .map(role -> {
                        CustomGrantedAuthority authority = new CustomGrantedAuthority();
                        authority.setAuthority(role.getRoleName());
                        return authority;
                    })
                    .toList();
            customUserDetails.setAuthorities(authorities);
            return customUserDetails;
        } else {
            throw new UsernameNotFoundException("User not found with email: " + username);
        }
    }
}
