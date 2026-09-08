package com.blogapp.controller;

import com.blogapp.dto.UserProfileResponse;
import com.blogapp.model.User;
import com.blogapp.security.UserPrincipal;
import com.blogapp.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/me")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<UserProfileResponse> getCurrentUser(@AuthenticationPrincipal UserPrincipal principal) {
        return ResponseEntity.ok(toDto(principal.getUser()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserProfileResponse> getUser(@PathVariable String id) {
        return ResponseEntity.ok(toDto(userService.getById(id)));
    }

    @PatchMapping("/me/bio")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<UserProfileResponse> updateBio(@RequestBody Map<String, String> body,
                                                           @AuthenticationPrincipal UserPrincipal principal) {
        User updated = userService.updateBio(principal.getId(), body.get("bio"));
        return ResponseEntity.ok(toDto(updated));
    }

    private UserProfileResponse toDto(User user) {
        List<String> roles = user.getRoles().stream().map(Enum::name).collect(Collectors.toList());
        return new UserProfileResponse(user.getId(), user.getName(), user.getEmail(), user.getBio(), roles, user.getCreatedAt());
    }
}
