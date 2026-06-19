package com.example.crowdfundingplatform.domain.dto.response;

import com.example.crowdfundingplatform.domain.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuthTokenResponse {

    private String token;
    private String tokenType;
    private Long userId;
    private String name;
    private String email;
    private Role role;
}