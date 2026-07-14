package com.example.chatweb.webscoket;

import com.example.chatweb.Config.JWTUserData;
import com.example.chatweb.Config.TokenService;
import com.example.chatweb.entity.User;
import com.example.chatweb.repositories.UserRepository;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Optional;
@Slf4j
@Component
@RequiredArgsConstructor
public class ChatChannelInterceptor implements ChannelInterceptor {

    private final TokenService tokenService;
    private final UserRepository userRepository;

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor accessor = MessageHeaderAccessor
                .getAccessor(message, StompHeaderAccessor.class);

        if (StompCommand.CONNECT.equals(accessor.getCommand())) {
            log.info("SESSION ATTRIBUTES: {}", accessor.getSessionAttributes());

            String token = null;
            Map<String, Object> sessionAttributes = accessor.getSessionAttributes();
            if (sessionAttributes != null) {
                token = (String) sessionAttributes.get("jwt");
            }

            Optional<JWTUserData> user = tokenService.validateToken(token);
            if (user.isEmpty()){
                throw new RuntimeException("token invalido");
            }
            User authenticated = userRepository.findByUsername(user.get().username())
                    .orElseThrow(() -> new RuntimeException("usuario nao encontrado"));
            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                    authenticated, null, authenticated.getAuthorities()
            );
            accessor.setUser(authenticationToken);
        }
        return message;
    }
}