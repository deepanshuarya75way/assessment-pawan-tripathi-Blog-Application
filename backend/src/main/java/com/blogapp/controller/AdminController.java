package com.blogapp.controller;

import com.blogapp.dto.UserProfileResponse;
import com.blogapp.model.User;
import com.blogapp.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

/**
 * All endpoints here require the ADMIN role.
 * Enforced both at the SecurityConfig level (/api/admin/**) and via @PreAuthorize for defense in depth.
 */
@RestController
@RequestMapping("/api/admin")
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {

    private final UserService userService;

    public AdminController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/users")
    public ResponseEntity<List<UserProfileResponse>> getAllUsers() {
        List<UserProfileResponse> users = userService.getAllUsers().stream()
                .map(u -> new UserProfileResponse(
                        u.getId(), u.getName(), u.getEmail(), u.getBio(),
                        u.getRoles().stream().map(Enum::name).collect(Collectors.toList()),
                        u.getCreatedAt()))
                .collect(Collectors.toList());
        return ResponseEntity.ok(users);
    }

    @DeleteMapping("/users/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable String id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }
}
