package com.example.crowdfundingplatform.service;

import com.example.crowdfundingplatform.domain.dto.request.LoginRequest;
import com.example.crowdfundingplatform.domain.dto.request.RegisterRequest;
import com.example.crowdfundingplatform.domain.dto.response.AuthTokenResponse;

public interface AuthService {

    AuthTokenResponse register(RegisterRequest request);

    AuthTokenResponse login(LoginRequest request);
}
