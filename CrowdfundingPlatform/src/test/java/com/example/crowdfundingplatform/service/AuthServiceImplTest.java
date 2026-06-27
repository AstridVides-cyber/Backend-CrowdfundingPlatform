package com.example.crowdfundingplatform.service;

import com.example.crowdfundingplatform.domain.dto.request.RegisterRequest;
import com.example.crowdfundingplatform.domain.entity.User;
import com.example.crowdfundingplatform.domain.enums.Role;
import com.example.crowdfundingplatform.exception.BadRequestException;
import com.example.crowdfundingplatform.repository.UserRepository;
import com.example.crowdfundingplatform.security.JwtUtil;
import com.example.crowdfundingplatform.service.implService.AuthServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceImplTest {

    @Mock private UserRepository userRepository;
    @Mock private PasswordEncoder passwordEncoder;
    @Mock private JwtUtil jwtUtil;
    @Mock private AuthenticationManager authenticationManager;

    @InjectMocks private AuthServiceImpl authService;

    @Test
    void register_ShouldThrowException_WhenEmailExists() {
        // Arrange
        RegisterRequest request = new RegisterRequest("Juan", "juan@test.com", "12345", Role.SPONSOR);
        when(userRepository.existsByEmail(request.getEmail())).thenReturn(true);

        // Act & Assert
        assertThrows(BadRequestException.class, () -> authService.register(request));
    }

    @Test
    void register_ShouldReturnToken_WhenDataIsValid() {
        RegisterRequest request = new RegisterRequest("Juan", "juan@test.com", "12345", Role.SPONSOR);
        when(userRepository.existsByEmail(request.getEmail())).thenReturn(false);
        when(passwordEncoder.encode(request.getPassword())).thenReturn("encodedPass");
        when(jwtUtil.generateToken(anyString())).thenReturn("fake-jwt-token");

        // Act
        var response = authService.register(request);

        // Assert
        assertNotNull(response);
        assertEquals("fake-jwt-token", response.getToken());
        verify(userRepository, times(1)).save(any(User.class));
    }
}