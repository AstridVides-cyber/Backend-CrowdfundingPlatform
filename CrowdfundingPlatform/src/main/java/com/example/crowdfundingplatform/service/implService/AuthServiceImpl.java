package com.example.crowdfundingplatform.service.implService;

import com.example.crowdfundingplatform.domain.dto.request.LoginRequest;
import com.example.crowdfundingplatform.domain.dto.request.RegisterRequest;
import com.example.crowdfundingplatform.domain.dto.response.AuthTokenResponse;
import com.example.crowdfundingplatform.domain.entity.User;
import com.example.crowdfundingplatform.exception.BadRequestException;
import com.example.crowdfundingplatform.repository.UserRepository;
import com.example.crowdfundingplatform.security.JwtUtil;
import com.example.crowdfundingplatform.service.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final AuthenticationManager authenticationManager;

    @Override
    public AuthTokenResponse register(RegisterRequest request) {

        // Verificar si el email ya existe
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new BadRequestException("El email ya está registrado");
        }

        // Crear el usuario
        User user = User.builder()
                .name(request.getName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(request.getRole())
                .build();

        userRepository.save(user);

        // Generar token
        String token = jwtUtil.generateToken(user.getEmail());

        return AuthTokenResponse.builder()
                .token(token)
                .tokenType("Bearer")
                .userId(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .role(user.getRole())
                .build();
    }

    @Override
    public AuthTokenResponse login(LoginRequest request) {

        // Autenticar al usuario
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );

        // Buscar el usuario
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new BadRequestException("Credenciales incorrectas"));

        // Generar token
        String token = jwtUtil.generateToken(user.getEmail());

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
