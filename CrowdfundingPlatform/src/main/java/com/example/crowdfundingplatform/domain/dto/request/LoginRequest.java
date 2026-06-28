package com.example.crowdfundingplatform.domain.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LoginRequest {

    @NotBlank(message = "El correo no puede estar vacío")
    @Email(message = "El correo debe tener un formato válido")
    private String email;

    @NotBlank(message = "La contraseña no puede estar vacía")
    private String password;
}