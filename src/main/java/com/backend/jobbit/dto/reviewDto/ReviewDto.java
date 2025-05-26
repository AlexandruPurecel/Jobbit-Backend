package com.backend.jobbit.dto.reviewDto;

import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ReviewDto {
    private Long reviewId;
    private Integer rating;
    private String comment;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    private Long reviewerId;
    private String reviewerFirstName;
    private String reviewerLastName;
    private Long reviewerProfilePictureId;

    private Long reviewedUserId;
    private String reviewedUserFirstName;
    private String reviewedUserLastName;

}
