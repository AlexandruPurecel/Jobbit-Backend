package com.backend.jobbit.service.reviewService;

import com.backend.jobbit.dto.reviewDto.ReviewDto;
import com.backend.jobbit.dto.reviewDto.ReviewRequestDto;
import com.backend.jobbit.dto.userDto.UserReviewStatsDto;
import com.backend.jobbit.persistence.Review;

import java.util.List;

public interface ReviewService {

    ReviewDto createReview(ReviewRequestDto request, Long reviewerId);

    UserReviewStatsDto getUserReviewStats(Long userId);

    List<ReviewDto> getReviewsForUser(Long userId);

    ReviewDto convertToDto(Review review);

    void deleteReview(Long id);

    ReviewDto updateReview(Long reviewId, ReviewRequestDto request, Long userId);

}
