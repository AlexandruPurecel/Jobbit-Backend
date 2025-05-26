package com.backend.jobbit.repository;

import com.backend.jobbit.persistence.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.repository.query.Param;
import com.backend.jobbit.dto.userDto.UserReviewStatsDto;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long> {

    List<Review> findByReviewedUserUserIdOrderByCreatedAtDesc(Long reviewedUserId);

    List<Review> findByReviewerUserIdOrderByCreatedAtDesc(Long reviewerId);

    boolean existsByReviewerUserIdAndReviewedUserUserId(Long reviewerId, Long reviewedUserId);

    @Query("SELECT AVG(r.rating) FROM Review r WHERE r.reviewedUser.userId = :userId")
    Double findAverageRatingByUserId(@Param("userId") Long userId);

    @Query("SELECT COUNT(r) FROM Review r WHERE r.reviewedUser.userId = :userId")
    Long countReviewsByUserId(@Param("userId") Long userId);

    @Query("SELECT COUNT(r) FROM Review r WHERE r.reviewedUser.userId = :userId AND r.rating = 5")
    Long countFiveStarReviews(@Param("userId") Long userId);

    @Query("SELECT COUNT(r) FROM Review r WHERE r.reviewedUser.userId = :userId AND r.rating = 4")
    Long countFourStarReviews(@Param("userId") Long userId);

    @Query("SELECT COUNT(r) FROM Review r WHERE r.reviewedUser.userId = :userId AND r.rating = 3")
    Long countThreeStarReviews(@Param("userId") Long userId);

    @Query("SELECT COUNT(r) FROM Review r WHERE r.reviewedUser.userId = :userId AND r.rating = 2")
    Long countTwoStarReviews(@Param("userId") Long userId);

    @Query("SELECT COUNT(r) FROM Review r WHERE r.reviewedUser.userId = :userId AND r.rating = 1")
    Long countOneStarReviews(@Param("userId") Long userId);
}
