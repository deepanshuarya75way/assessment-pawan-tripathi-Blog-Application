package com.blogapp.service;

import com.blogapp.dto.AuthResponse;
import com.blogapp.dto.LoginRequest;
import com.blogapp.dto.RegisterRequest;
import com.blogapp.exception.DuplicateResourceException;
import com.blogapp.model.Role;
import com.blogapp.model.User;
import com.blogapp.repository.UserRepository;
import com.blogapp.security.JwtUtil;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final AuthenticationManager authenticationManager;

    public AuthService(UserRepository userRepository,
                        PasswordEncoder passwordEncoder,
                        JwtUtil jwtUtil,
                        AuthenticationManager authenticationManager) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
        this.authenticationManager = authenticationManager;
    }

    public AuthResponse register(RegisterRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new DuplicateResourceException("An account with this email already exists");
        }

        User user = new User();
        user.setName(request.getName());
        user.setEmail(request.getEmail().toLowerCase());
        user.setPassword(passwordEncoder.encode(request.getPassword()));

        Set<Role> roles = new HashSet<>();
        roles.add(Role.USER);
        user.setRoles(roles);

        User saved = userRepository.save(user);

        List<String> roleNames = roleNames(saved);
        String token = jwtUtil.generateToken(saved.getId(), saved.getEmail(), roleNames);

        return new AuthResponse(token, saved.getId(), saved.getName(), saved.getEmail(), roleNames);
    }

    public AuthResponse login(LoginRequest request) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getEmail().toLowerCase(), request.getPassword())
            );
        } catch (Exception ex) {
            throw new BadCredentialsException("Invalid email or password");
        }

        User user = userRepository.findByEmail(request.getEmail().toLowerCase())
                .orElseThrow(() -> new BadCredentialsException("Invalid email or password"));

        List<String> roleNames = roleNames(user);
        String token = jwtUtil.generateToken(user.getId(), user.getEmail(), roleNames);

        return new AuthResponse(token, user.getId(), user.getName(), user.getEmail(), roleNames);
    }

    private List<String> roleNames(User user) {
        return user.getRoles().stream().map(Enum::name).collect(Collectors.toList());
    }
}
