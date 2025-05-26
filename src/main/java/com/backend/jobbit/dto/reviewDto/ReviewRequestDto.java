package com.backend.jobbit.dto.reviewDto;
import jakarta.validation.constraints.*;
import lombok.*;



@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ReviewRequestDto {

    @NotNull
    @Min(1)
    @Max(5)
    private Integer rating;

    private String comment;

    @NotNull
    private Long reviewedUserId;
}
