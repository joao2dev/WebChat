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
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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
    public void sendMessageInGroup(@Payload ChatMessage message, @AuthenticationPrincipal User sender){
        Group grupo = groupService.findByName(message.destination());
        groupMessageService.createMessage(sender,grupo,message.content());
        messagingTemplate.convertAndSend("/topic/group/" + message.destination(),message);
    }
    @MessageMapping("/chat.direct")
    public void directMessage(@Payload ChatMessage message,@AuthenticationPrincipal User sender){
        User userB = userService.findByUsername(message.destination());
        Conversation conversation = conversationService.findByUsers(sender,userB);
        directMessageService.createMessage(sender,conversation, message.content());
        messagingTemplate.convertAndSend("/queue/user/" + message.destination(),message);
    }

}
