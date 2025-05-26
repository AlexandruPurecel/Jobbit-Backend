package com.backend.jobbit.dto.userDto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserReviewStatsDto {
    private Long userId;
    private Double averageRating;
    private Long totalReviews;
    private Long fiveStars;
    private Long fourStars;
    private Long threeStars;
    private Long twoStars;
    private Long oneStar;
}
