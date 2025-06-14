package com.backend.jobbit.controller;

import com.backend.jobbit.config.security.JwtUtil;
import com.backend.jobbit.dto.messageDto.ConversationDto;
import com.backend.jobbit.dto.messageDto.MessageDto;
import com.backend.jobbit.service.MessageService;
import io.jsonwebtoken.Claims;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("/api/messages")
@AllArgsConstructor
public class MessageController {

    private final MessageService messageService;
    private final JwtUtil jwtUtil;

    @PostMapping("/send")
    public ResponseEntity<MessageDto> sendMessage(
            @RequestHeader("Authorization") String authHeader,
            @RequestBody Map<String, Object> payload) {

        String token = authHeader.substring(7);

        if (!jwtUtil.validateToken(token)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        Claims claims = jwtUtil.extractAllClaims(token);
        Long senderId = claims.get("userId", Long.class);
        Long recipientId = Long.parseLong(payload.get("recipientId").toString());
        String content = payload.get("content").toString();

        MessageDto messageDto = messageService.sendMessage(senderId, recipientId, content);
        return ResponseEntity.ok(messageDto);
    }

    @GetMapping("/conversation/{conversationId}")
    public ResponseEntity<List<MessageDto>> getConversationMessages(
            @RequestHeader("Authorization") String authHeader,
            @PathVariable Long conversationId) {

        String token = authHeader.substring(7);

        if (!jwtUtil.validateToken(token)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        List<MessageDto> messages = messageService.getConversationMessages(conversationId);
        return ResponseEntity.ok(messages);
    }

    @GetMapping("/conversations")
    public ResponseEntity<List<ConversationDto>> getUserConversations(
            @RequestHeader("Authorization") String authHeader) {

        String token = authHeader.substring(7);

        if (!jwtUtil.validateToken(token)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        Claims claims = jwtUtil.extractAllClaims(token);
        Long userId = claims.get("userId", Long.class);

        List<ConversationDto> conversations = messageService.getUserConversations(userId);
        return ResponseEntity.ok(conversations);
    }

    @PostMapping("/read/{messageId}")
    public ResponseEntity<Void> markMessageAsRead(
            @RequestHeader("Authorization") String authHeader,
            @PathVariable Long messageId) {

        String token = authHeader.substring(7);

        if (!jwtUtil.validateToken(token)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        messageService.markAsRead(messageId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/unread-count")
    public ResponseEntity<Map<String, Integer>> getUnreadCount(
            @RequestHeader("Authorization") String authHeader) {

        String token = authHeader.substring(7);

        if (!jwtUtil.validateToken(token)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        Claims claims = jwtUtil.extractAllClaims(token);
        Long userId = claims.get("userId", Long.class);

        int count = messageService.getUnreadCount(userId);
        Map<String, Integer> response = new HashMap<>();
        response.put("unreadCount", count);

        return ResponseEntity.ok(response);
    }
}
