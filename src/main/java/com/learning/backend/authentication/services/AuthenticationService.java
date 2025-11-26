package com.learning.backend.authentication.services;

import com.learning.backend.authentication.entities.Role;
import com.learning.backend.authentication.entities.Session;
import com.learning.backend.authentication.entities.User;
import com.learning.backend.authentication.models.UserInfo;
import com.learning.backend.authentication.repository.RoleRepository;
import com.learning.backend.authentication.repository.SessionRepository;
import com.learning.backend.authentication.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.temporal.TemporalAmount;
import java.util.Arrays;
import java.util.Base64;
import java.util.List;

import static com.learning.backend.authentication.services.AESEncyptionService.encrypt;
import static java.time.temporal.ChronoUnit.MONTHS;

@Service
public class AuthenticationService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SessionRepository sessionRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private String algorithm = "AES";
    // Static base64-encoded key (must be 16 bytes for AES-128)
    private static final String STATIC_KEY_BASE64 = "MTIzNDU2Nzg5MGFiY2RlZg=="; // "mySuperSecretKey123" in base64
    private final SecretKey secretKey = new SecretKeySpec(Base64.getDecoder().decode(STATIC_KEY_BASE64), algorithm);

    public AuthenticationService() {
    }

    public void signup(UserInfo userInfo) {
        if(userInfo.getUserName()==null || userInfo.getPassword()==null || userInfo.getEmail()==null) {
            throw new RuntimeException("Invalid data");
        }
       if(isAlreadyRegistered(userInfo)) {
           throw new RuntimeException("User already registered");
       }
       User user = prepareUserEntity(userInfo);
       List<Role> roles = createDefaultRolesForUser(user);
        user.setRoles(roles);
       userRepository.save(user);
    }

    private User prepareUserEntity(UserInfo userInfo) {
        User user = new User();
        user.setUsername(userInfo.getUserName());
        user.setPassword(passwordEncoder.encode(userInfo.getPassword()));
        user.setEmail(userInfo.getEmail());
        return user;
    }

    private boolean isAlreadyRegistered(UserInfo userInfo) {
        return userRepository.findByEmail(userInfo.getEmail()) != null;
    }

    public String login(UserInfo userInfo) {
        User user = userRepository.findByEmail(userInfo.getEmail());
        if(user == null || !passwordEncoder.matches(userInfo.getPassword(), user.getPassword())) {
            throw new RuntimeException("Invalid credentials");
        }
        userInfo.setRoles(user.getRoles());
        String token = createJWTToken(userInfo);
        userInfo.setUserId(user.getId());
        createSession(userInfo, token);
        return token;

    }

    private String createJWTToken(UserInfo userInfo) {
        StringBuilder authoritiesBuilder = new StringBuilder();
        if (userInfo != null && userInfo.getRoles() != null) {
            for (Role role : userInfo.getRoles()) {
                if (authoritiesBuilder.length() > 0) authoritiesBuilder.append(",");
                authoritiesBuilder.append(role.getRoleName());
            }
        }
        String header = Base64.getEncoder().encodeToString( ("{ \"alg\": \"" + algorithm +"\" }").getBytes());
        String payload = Base64.getEncoder().encodeToString( ("{ \"user\": \"" + userInfo.getUserName()  +
                         "\", \"email\": \"" + userInfo.getEmail() +
                         "\", \"authorities\": \"" + authoritiesBuilder.toString() + "\" }").getBytes());
        String signature = null;
        try {
            signature = AESEncyptionService.encrypt(header+"."+payload, secretKey);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return header + "." + payload + "." + signature;
    }

    public void assignRoleToUser(Long userId, Long roleId) {
        // Logic to assign role to user
    }

    public void createSession(UserInfo userInfo, String token) {
        sessionRepository.save(new Session(token, userInfo.getUserId(), userInfo.getIPAddress(),
                userInfo.getDeviceInfo(), LocalDateTime.now().plus(1, MONTHS).toEpochSecond(ZoneOffset.of("+05:30"))));
    }

    public void invalidateSession(Long sessionId) {
        // Logic to invalidate a user session
    }

    public String getUserDetails(String token) {
        String header = token.substring(0, token.indexOf("."));
        String payload = token.substring(token.indexOf(".")+1, token.lastIndexOf("."));
        String signature = token.substring(token.lastIndexOf(".")+1);
        String expectedSignature = null;
        try {
            expectedSignature = encrypt(header+"."+payload, secretKey);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        if(expectedSignature.equals(signature)) {
            return new String(Base64.getDecoder().decode(payload));
        } else {
            throw new RuntimeException("Invalid token");
        }
    }

    public List<Role> createDefaultRolesForUser(User user) {
        Role adminRole = new Role();
        adminRole.setRoleName("ADMIN");

        Role userRole = new Role();
        userRole.setRoleName("USER");

        Role guestRole = new Role();
        guestRole.setRoleName("GUEST");
        return Arrays.asList(adminRole, userRole, guestRole);
    }
}
