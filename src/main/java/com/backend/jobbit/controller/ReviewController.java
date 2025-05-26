package com.backend.jobbit.controller;

import com.backend.jobbit.config.security.JwtUtil;
import com.backend.jobbit.dto.reviewDto.ReviewDto;
import com.backend.jobbit.dto.reviewDto.ReviewRequestDto;
import com.backend.jobbit.dto.userDto.UserReviewStatsDto;
import com.backend.jobbit.service.reviewService.ReviewService;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/reviews")
public class ReviewController {

    @Autowired
    private ReviewService reviewService;

    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping
    public ResponseEntity<ReviewDto> createReview(@Valid @RequestBody ReviewRequestDto request,
                                                  HttpServletRequest httpRequest) {
        try {
            Long reviewerId = getUserIdFromRequest(httpRequest);

            ReviewDto review = reviewService.createReview(request, reviewerId);
            return ResponseEntity.ok(review);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(null);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }


    @GetMapping("/user/{userId}")
    public ResponseEntity<List<ReviewDto>> getReviewsForUser(@PathVariable Long userId) {
        try {
            List<ReviewDto> reviews = reviewService.getReviewsForUser(userId);
            return ResponseEntity.ok(reviews);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @DeleteMapping("/{reviewId}")
    public ResponseEntity<Void> deleteReview(@PathVariable Long reviewId,
                                             HttpServletRequest httpRequest) {
        try {
            String userRole = getUserRoleFromRequest(httpRequest);

            if (!"ADMIN".equals(userRole)) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
            }

            reviewService.deleteReview(reviewId);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }


    @GetMapping("/user/{userId}/stats")
    public ResponseEntity<UserReviewStatsDto> getUserReviewStats(@PathVariable Long userId) {
        try {
            UserReviewStatsDto stats = reviewService.getUserReviewStats(userId);
            return ResponseEntity.ok(stats);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    private Long getUserIdFromRequest(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String jwt = authHeader.substring(7);

            try {
                Claims claims = jwtUtil.extractAllClaims(jwt);
                return claims.get("userId", Long.class);
            } catch (Exception e) {
                throw new RuntimeException("Invalid token or userId not found in token", e);
            }
        }

        throw new RuntimeException("No valid JWT token found");
    }

    private String getUserRoleFromRequest(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String jwt = authHeader.substring(7);

            try {
                Claims claims = jwtUtil.extractAllClaims(jwt);
                return claims.get("role", String.class);
            } catch (Exception e) {
                throw new RuntimeException("Invalid token or role not found in token", e);
            }
        }

        throw new RuntimeException("No valid JWT token found");
    }

    @PutMapping("/{reviewId}")
    public ResponseEntity<ReviewDto> updateReview(@PathVariable Long reviewId,
                                                  @Valid @RequestBody ReviewRequestDto request,
                                                  HttpServletRequest httpRequest) {
        try {
            Long userId = getUserIdFromRequest(httpRequest);
            ReviewDto updatedReview = reviewService.updateReview(reviewId, request, userId);
            return ResponseEntity.ok(updatedReview);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
