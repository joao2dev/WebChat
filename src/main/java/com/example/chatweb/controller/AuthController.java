package com.example.chatweb.controller;

import com.example.chatweb.Config.TokenService;
import com.example.chatweb.Service.UserService;
import com.example.chatweb.dto.LoginRequest;
import com.example.chatweb.dto.RegisterRequest;
import com.example.chatweb.entity.User;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final TokenService tokenService;
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    @PostMapping("/login")
    ResponseEntity<String> loginRequest(@RequestBody LoginRequest loginRequest, HttpServletResponse response){
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(loginRequest.username(),loginRequest.password());
        Authentication authenticate = authenticationManager.authenticate(authenticationToken);
        User user = (User) authenticate.getPrincipal();
        String token = tokenService.generateToken(user);
        Cookie cookie = new Cookie("jwt",token);
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        cookie.setMaxAge(7200);
        response.addCookie(cookie);
        return ResponseEntity.ok("logado");
    }
    @PostMapping("/registrar")
    ResponseEntity<String> registerRequest(@RequestBody RegisterRequest registerRequest){
        User newUser = new User(null,registerRequest.username(),registerRequest.email(),registerRequest.password());
        newUser.setPassword(passwordEncoder.encode(newUser.getPassword()));
        userService.createUser(newUser);
        return ResponseEntity.status(HttpStatus.CREATED).body("usuario registrado");
    }
}
