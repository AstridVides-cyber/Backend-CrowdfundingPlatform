package com.example.crowdfundingplatform.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final UserDetailsService userDetailsService;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        // Obtener el header Authorization
        String authHeader = request.getHeader("Authorization");

        // Si no tiene token, continuar sin autenticar
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        // Extraer el token quitando "Bearer "
        String token = authHeader.substring(7);

        // Si el email es válido y no hay autenticación previa
        if (email != null && SecurityContextHolder.getContext().getAuthentication() == null) {

            // Cargar el usuario de la base de datos
            var userDetails = userDetailsService.loadUserByUsername(email);

            // Validar el token
            if (jwtUtil.validateToken(token)) {
                    // Crear la autenticación y meterla en el contexto
                var authToken = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities()
                );
                authToken.setDetails(
                        new WebAuthenticationDetailsSource().buildDetails(request)
                );SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }
        // Continuar con el siguiente filtro
        filterChain.doFilter(request, response);
    }
}
