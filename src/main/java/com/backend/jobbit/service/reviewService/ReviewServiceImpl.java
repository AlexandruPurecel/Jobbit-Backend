package com.backend.jobbit.service.reviewService;

import com.backend.jobbit.dto.reviewDto.ReviewDto;
import com.backend.jobbit.dto.reviewDto.ReviewRequestDto;
import com.backend.jobbit.dto.userDto.UserReviewStatsDto;
import com.backend.jobbit.exception.ResourceNotFoundException;
import com.backend.jobbit.persistence.Review;
import com.backend.jobbit.persistence.model.User;
import com.backend.jobbit.repository.ReviewRepository;
import com.backend.jobbit.repository.UserRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class ReviewServiceImpl implements ReviewService{

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private UserRepo userRepository;

    @Override
    public ReviewDto createReview(ReviewRequestDto request, Long reviewerId) {
        if (reviewerId.equals(request.getReviewedUserId())) {
            throw new IllegalArgumentException("You cannot review yourself");
        }

        if (reviewRepository.existsByReviewerUserIdAndReviewedUserUserId(reviewerId, request.getReviewedUserId())) {
            throw new IllegalArgumentException("You have already reviewed this user");
        }

        User reviewer = userRepository.findById(reviewerId)
                .orElseThrow(() -> new ResourceNotFoundException("Reviewer not found"));

        User reviewedUser = userRepository.findById(request.getReviewedUserId())
                .orElseThrow(() -> new ResourceNotFoundException("User to review not found"));

        Review review = new Review();
        review.setRating(request.getRating());
        review.setComment(request.getComment());
        review.setReviewer(reviewer);
        review.setReviewedUser(reviewedUser);

        Review savedReview = reviewRepository.save(review);
        return convertToDto(savedReview);
    }

    @Override
    public UserReviewStatsDto getUserReviewStats(Long userId) {
        Double avgRating = reviewRepository.findAverageRatingByUserId(userId);
        Long totalReviews = reviewRepository.countReviewsByUserId(userId);

        Long fiveStars = reviewRepository.countFiveStarReviews(userId);
        Long fourStars = reviewRepository.countFourStarReviews(userId);
        Long threeStars = reviewRepository.countThreeStarReviews(userId);
        Long twoStars = reviewRepository.countTwoStarReviews(userId);
        Long oneStar = reviewRepository.countOneStarReviews(userId);

        return new UserReviewStatsDto(
                userId,
                avgRating != null ? avgRating : 0.0,
                totalReviews != null ? totalReviews : 0L,
                fiveStars != null ? fiveStars : 0L,
                fourStars != null ? fourStars : 0L,
                threeStars != null ? threeStars : 0L,
                twoStars != null ? twoStars : 0L,
                oneStar != null ? oneStar : 0L
        );
    }

    @Override
    public List<ReviewDto> getReviewsForUser(Long userId) {
        List<Review> reviews = reviewRepository.findByReviewedUserUserIdOrderByCreatedAtDesc(userId);
        return reviews.stream().map(this::convertToDto).collect(Collectors.toList());
    }

    @Override
    public ReviewDto convertToDto(Review review) {
        ReviewDto dto = new ReviewDto();
        dto.setReviewId(review.getReviewId());
        dto.setRating(review.getRating());
        dto.setComment(review.getComment());
        dto.setCreatedAt(review.getCreatedAt());
        dto.setUpdatedAt(review.getUpdatedAt());

        dto.setReviewerId(review.getReviewer().getUserId());
        dto.setReviewerFirstName(review.getReviewer().getFirstName());
        dto.setReviewerLastName(review.getReviewer().getLastName());
        dto.setReviewerProfilePictureId(review.getReviewer().getProfileImage() != null ?
                review.getReviewer().getProfileImage().getId() : null);

        dto.setReviewedUserId(review.getReviewedUser().getUserId());
        dto.setReviewedUserFirstName(review.getReviewedUser().getFirstName());
        dto.setReviewedUserLastName(review.getReviewedUser().getLastName());

        return dto;
    }

    @Override
    public void deleteReview(Long id) {
        Review review = reviewRepository.findById(id)
                .orElseThrow( () -> new ResourceNotFoundException("Review not found"));

        reviewRepository.delete(review);
    }

    @Override
    public ReviewDto updateReview(Long reviewId, ReviewRequestDto request, Long userId) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new RuntimeException("Review not found"));

        if (!review.getReviewer().getUserId().equals(userId)) {
            throw new IllegalArgumentException("You can only edit your own reviews");
        }

        review.setRating(request.getRating());
        review.setComment(request.getComment());

        Review savedReview = reviewRepository.save(review);
        return convertToDto(savedReview);
    }
}
