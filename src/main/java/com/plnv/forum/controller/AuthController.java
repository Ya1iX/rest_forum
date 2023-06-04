package com.plnv.forum.controller;

import com.plnv.forum.model.LoginRequest;
import com.plnv.forum.model.RegisterRequest;
import com.plnv.forum.model.Response;
import com.plnv.forum.model.Role;
import com.plnv.forum.auth.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService service;

    @PostMapping("/register")
    public ResponseEntity<Response> register(
            @RequestBody RegisterRequest request
    ) {
        return ResponseEntity.ok(service.register(request, Role.USER));
    }

    @PostMapping("/login")
    public ResponseEntity<Response> login(
            @RequestBody LoginRequest request
    ) {
        return ResponseEntity.ok(service.login(request));
    }

    @PostMapping("/makeFirstAdmin")
    public ResponseEntity<Response> firstAdmin(
            @RequestBody RegisterRequest request
    ) {
        return ResponseEntity.ok(service.makeFirstAdmin(request));
    }
}
