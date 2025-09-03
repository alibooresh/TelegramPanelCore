package com.telegram.core.controller;

import com.telegram.core.service.ChatService;
import com.telegram.core.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController("/chat")
public class ChatController {

    private final ChatService chatService;

    public ChatController(ChatService chatService) {
        this.chatService = chatService;
    }

    @GetMapping("/getChats")
    public ResponseEntity<?> getChats(@RequestParam int limit) {
        return ResponseEntity.ok(chatService.getChats(limit));
    }

    @GetMapping("/getChat")
    public ResponseEntity<?> getChats(@RequestParam Long chatId) {
        return ResponseEntity.ok(chatService.getChat(chatId));
    }

    @GetMapping("/sendMessage")
    public ResponseEntity<?> sendMessage(@RequestParam Long chatId, @RequestParam String message) {
        return ResponseEntity.ok(chatService.sendMessage(chatId,message));
    }
}
