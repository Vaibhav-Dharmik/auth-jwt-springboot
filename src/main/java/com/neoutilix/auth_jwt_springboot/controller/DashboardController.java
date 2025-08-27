package com.neoutilix.auth_jwt_springboot.controller;

import com.neoutilix.auth_jwt_springboot.dto.UserDto;
import com.neoutilix.auth_jwt_springboot.service.UserService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
public class DashboardController {

    private final UserService userService;

    @GetMapping("/dashboard")
    @PreAuthorize("hasAnyRole('ADMIN','USER')")
    public ResponseEntity<String> dashboard() {
        return ResponseEntity.ok("Welcome to dashboard");
    }

    @GetMapping("/admin/metrics")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> adminMetrics() {
        return ResponseEntity.ok("Admin metrics placeholder");
    }

    @GetMapping("/profile")
    public ResponseEntity<UserDto> profile() {
        return ResponseEntity.ok(userService.getCurrentUserProfile());
    }
}
