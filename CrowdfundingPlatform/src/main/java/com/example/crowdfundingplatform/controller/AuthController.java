package com.example.crowdfundingplatform.controller;

import com.example.crowdfundingplatform.domain.dto.request.LoginRequest;
import com.example.crowdfundingplatform.domain.dto.request.RegisterRequest;
import com.example.crowdfundingplatform.domain.dto.response.AuthTokenResponse;
import com.example.crowdfundingplatform.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    // Inyección de la capa de negocio (Servicio)
    private final AuthService authService;

    // POST - Registro de un nuevo usuario (CREATOR, SPONSOR o ADMIN)
    @PostMapping("/register")
    public ResponseEntity<AuthTokenResponse> register(@Valid @RequestBody RegisterRequest request) {
        AuthTokenResponse response = authService.register(request);
        // 201: se creó un recurso nuevo (la cuenta)
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    // POST - Inicio de sesión, devuelve el token JWT
    @PostMapping("/login")
    public ResponseEntity<AuthTokenResponse> login(@Valid @RequestBody LoginRequest request) {
        AuthTokenResponse response = authService.login(request);
        // 200: no se crea recurso, solo se autentica
        return ResponseEntity.ok(response);
    }
}
