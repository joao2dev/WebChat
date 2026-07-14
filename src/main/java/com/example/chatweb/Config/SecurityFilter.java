package com.example.chatweb.Config;

import com.example.chatweb.entity.User;
import com.example.chatweb.repositories.UserRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class SecurityFilter extends OncePerRequestFilter {

    private final TokenService tokenService;
    private final UserRepository userRepository;



    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String token = extractToken(request);
        System.out.println("PATH: " + request.getServletPath() + " | TOKEN: " + token);
        if (token != null){
            Optional<JWTUserData> username = tokenService.validateToken(token);
            System.out.println("USERNAME: " + username);
            if (username.isPresent()){
                User user = userRepository.findByUsername(username.get().username())
                        .orElseThrow(() -> new RuntimeException("usuario nao encontrado"));
                var authentication = new UsernamePasswordAuthenticationToken(
                        user, null, user.getAuthorities()
                );
                SecurityContextHolder.getContext().setAuthentication(authentication);
                System.out.println("AUTHENTICATION: " + SecurityContextHolder.getContext().getAuthentication());
            }
        }
        filterChain.doFilter(request, response);
    }
    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getServletPath();
        return path.equals("/auth/login")
                || path.equals("/auth/registrar")
                || path.startsWith("/ws"); // adiciona essa linha
    }
    private String extractToken(HttpServletRequest request) {
        if (request.getCookies() == null) {
            return null;
        }
        for (Cookie cookie : request.getCookies()){
            if ("jwt".equals(cookie.getName())){
                return cookie.getValue();
            }
        }
        return null;
    }
}
