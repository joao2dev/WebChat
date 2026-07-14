package com.example.chatweb.webscoket;

import com.example.chatweb.Service.*;
import com.example.chatweb.dto.ChatMessage;
import com.example.chatweb.entity.Conversation;
import com.example.chatweb.entity.Group;
import com.example.chatweb.entity.GroupMessage;
import com.example.chatweb.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;

@Controller
@RequiredArgsConstructor
public class ChatController {
    private final GroupMessageService groupMessageService;
    private final DirectMessageService directMessageService;
    private final SimpMessagingTemplate messagingTemplate;
    private final GroupService groupService;
    private final ConversationService conversationService;
    private final UserService userService;

    @MessageMapping("/chat.grupo")
    public void sendMessageInGroup(@Payload ChatMessage message,
                                   SimpMessageHeaderAccessor headerAccessor){
        UsernamePasswordAuthenticationToken auth =
                (UsernamePasswordAuthenticationToken) headerAccessor.getUser();
        User sender = (User) auth.getPrincipal();
        User senderManaged = userService.findByUsername(sender.getUsername());
        Group grupo = groupService.findByName(message.destination());
        groupMessageService.createMessage(senderManaged, grupo, message.content());
        messagingTemplate.convertAndSend("/topic/grupo/" + message.destination(), message);
    }

    @MessageMapping("/chat.direct")
    public void directMessage(@Payload ChatMessage message, SimpMessageHeaderAccessor headerAccessor){
        System.out.println("MENSAGEM RECEBIDA NO CHAT.DIRECT: " + message.content());
        UsernamePasswordAuthenticationToken auth =
                (UsernamePasswordAuthenticationToken) headerAccessor.getUser();
        User sender = (User) auth.getPrincipal();
        User senderManaged = userService.findByUsername(sender.getUsername());
        User userB = userService.findByUsername(message.destination());
        System.out.println("SENDER: " + senderManaged.getUsername() + " | USERB: " + userB.getUsername());
        Conversation conversation = conversationService.findByUsers(senderManaged, userB);
        System.out.println("CONVERSATION ID: " + conversation.getId());
        directMessageService.createMessage(senderManaged, conversation, message.content());
        System.out.println("MENSAGEM SALVA NO BANCO");
        messagingTemplate.convertAndSend("/queue/user/" + message.destination(), message);
        messagingTemplate.convertAndSend("/queue/user/" + sender.getUsername(), message);
        System.out.println("MENSAGEM ENVIADA PARA OS CANAIS"); // envia pra si mesmo
    }

}
