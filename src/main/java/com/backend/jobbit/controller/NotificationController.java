package com.backend.jobbit.controller;

import com.backend.jobbit.config.security.JwtUtil;
import com.backend.jobbit.dto.notificationsDto.NotificationDto;
import com.backend.jobbit.service.notificationService.NotificationService;
import io.jsonwebtoken.Claims;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/notifications")
@AllArgsConstructor
@CrossOrigin("*")
public class NotificationController {

    private final NotificationService notificationService;
    private final JwtUtil jwtUtil;

    @GetMapping
    public ResponseEntity<List<NotificationDto>> getUserNotifications(
            @RequestHeader("Authorization") String authHeader) {

        String token = authHeader.substring(7); // Remove "Bearer " prefix

        if (!jwtUtil.validateToken(token)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        Claims claims = jwtUtil.extractAllClaims(token);
        Long userId = claims.get("userId", Long.class);

        List<NotificationDto> notifications = notificationService.getUserNotifications(userId);
        return ResponseEntity.ok(notifications);
    }

    @GetMapping("/unread")
    public ResponseEntity<List<NotificationDto>> getUnreadNotifications(
            @RequestHeader("Authorization") String authHeader) {

        String token = authHeader.substring(7);

        if (!jwtUtil.validateToken(token)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        Claims claims = jwtUtil.extractAllClaims(token);
        Long userId = claims.get("userId", Long.class);

        List<NotificationDto> notifications = notificationService.getUnreadNotifications(userId);
        return ResponseEntity.ok(notifications);
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

        int count = notificationService.getUnreadCount(userId);
        Map<String, Integer> response = new HashMap<>();
        response.put("unreadCount", count);

        return ResponseEntity.ok(response);
    }

    @PostMapping("/read/{notificationId}")
    public ResponseEntity<Void> markAsRead(
            @RequestHeader("Authorization") String authHeader,
            @PathVariable Long notificationId) {

        String token = authHeader.substring(7);

        if (!jwtUtil.validateToken(token)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        Claims claims = jwtUtil.extractAllClaims(token);
        Long userId = claims.get("userId", Long.class);

        notificationService.markAsRead(notificationId);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/read-all")
    public ResponseEntity<Void> markAllAsRead(
            @RequestHeader("Authorization") String authHeader) {

        String token = authHeader.substring(7);

        if (!jwtUtil.validateToken(token)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        Claims claims = jwtUtil.extractAllClaims(token);
        Long userId = claims.get("userId", Long.class);

        notificationService.markAllAsRead(userId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{notificationId}")
    public ResponseEntity<Void> deleteNotification(
            @RequestHeader("Authorization") String authHeader,
            @PathVariable Long notificationId) {

        String token = authHeader.substring(7);

        if (!jwtUtil.validateToken(token)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        Claims claims = jwtUtil.extractAllClaims(token);
        Long userId = claims.get("userId", Long.class);

        notificationService.deleteNotification(notificationId, userId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/clear-all")
    public ResponseEntity<Void> clearAllNotifications(
            @RequestHeader("Authorization") String authHeader) {

        String token = authHeader.substring(7);

        if (!jwtUtil.validateToken(token)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        Claims claims = jwtUtil.extractAllClaims(token);
        Long userId = claims.get("userId", Long.class);

        notificationService.clearAllNotifications(userId);
        return ResponseEntity.ok().build();
    }
}