package com.telegram.core.controller;

import com.telegram.core.service.MessageService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController("/message")
public class MessageController {

    private final MessageService messageService;

    public MessageController(MessageService messageService) {
        this.messageService = messageService;
    }


    @GetMapping("/getMessages")
    public ResponseEntity<?> getMessages(@RequestParam Long chatId, @RequestParam long[] messageIds) {
        return ResponseEntity.ok(messageService.getMessages(chatId,messageIds));
    }

    @GetMapping("/getMessageProperties")
    public ResponseEntity<?> getMessageProperties(@RequestParam Long chatId, @RequestParam Long messageId) {
        return ResponseEntity.ok(messageService.getMessageProperties(chatId, messageId));
    }

    @GetMapping("/getMessageReadDate")
    public ResponseEntity<?> getMessageReadDate(@RequestParam Long chatId, @RequestParam Long messageId) {
        return ResponseEntity.ok(messageService.getMessageReadDate(chatId, messageId));
    }

}
