package com.learning.backend.authentication.models;

import com.learning.backend.authentication.entities.Role;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class UserInfo {
    private String email;
    private String password;
    private String userName;
    private String deviceInfo;
    private String IPAddress;
    private int userId;
    private List<Role> roles;
}
