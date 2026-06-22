package com.example.crowdfundingplatform.service;

import com.example.crowdfundingplatform.domain.dto.request.LoginRequest;
import com.example.crowdfundingplatform.domain.dto.request.RegisterRequest;
import com.example.crowdfundingplatform.domain.dto.response.AuthTokenResponse;
import com.example.crowdfundingplatform.domain.entity.User;
import com.example.crowdfundingplatform.exception.BadRequestException;
import com.example.crowdfundingplatform.exception.UnauthorizedException;
import com.example.crowdfundingplatform.repository.UserRepository;
import com.example.crowdfundingplatform.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final AuthenticationManager authenticationManager;

    // Registro de un nuevo usuario (CREATOR, SPONSOR o ADMIN)
    public AuthTokenResponse register(RegisterRequest request) {
        // Regla de negocio: no se permiten correos duplicados
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new BadRequestException("El correo ya está registrado");
        }

        User user = User.builder()
                .name(request.getName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword())) // se guarda encriptada con BCrypt
                .role(request.getRole())
                .build();

        User savedUser = userRepository.save(user);

        String token = jwtUtil.generateToken(savedUser.getEmail());
        return buildAuthResponse(savedUser, token);
    }

    // Inicio de sesión
    public AuthTokenResponse login(LoginRequest request) {
        // Valida email + contraseña usando el AuthenticationManager (incluye la verificación BCrypt)
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
            );
        } catch (AuthenticationException e) {
            throw new UnauthorizedException("Credenciales inválidas");
        }

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new UnauthorizedException("Credenciales inválidas"));

        String token = jwtUtil.generateToken(user.getEmail());
        return buildAuthResponse(user, token);
    }

    // Método auxiliar para armar la respuesta con el token
    private AuthTokenResponse buildAuthResponse(User user, String token) {
        return AuthTokenResponse.builder()
                .token(token)
                .tokenType("Bearer")
                .userId(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .role(user.getRole())
                .build();
    }
}