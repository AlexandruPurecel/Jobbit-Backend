package com.backend.jobbit.dto.jobDto;

import com.backend.jobbit.dto.locationDto.LocationDto;
import lombok.*;

import java.time.LocalDateTime;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class JobDto {

    private Long jobId;
    private String title;
    private String description;
    private Double price;
    private LocalDateTime createdAt;
    private Long postedById;
    private String postedByName;
    private Long categoryId;
    private String categoryName;
    private LocationDto locationDto;

}
