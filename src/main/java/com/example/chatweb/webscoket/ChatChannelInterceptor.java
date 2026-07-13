package com.example.chatweb.webscoket;

import com.example.chatweb.Config.JWTUserData;
import com.example.chatweb.Config.TokenService;
import com.example.chatweb.entity.User;
import com.example.chatweb.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Component;

import java.util.Optional;

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
            // extrai o token do header da mensagem STOMP
            String token = accessor.getFirstNativeHeader("Authorization");
            if (token != null && token.startsWith("Bearer ")) {
                token = token.substring(7);
            }
            Optional<JWTUserData> user = tokenService.validateToken(token);
            if (user.isEmpty()){
                throw new RuntimeException("token invalido");
            }
            User authenticated = userRepository.findByUsername(user.get().username())
                    .orElseThrow(() -> new RuntimeException("usuario nao encontrado"));
            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(authenticated,null,authenticated.getAuthorities());
            accessor.setUser(authenticationToken);
        }
        return message;
    }
}