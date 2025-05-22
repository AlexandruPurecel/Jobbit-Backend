package com.backend.jobbit.dto.categoryDto;

import com.backend.jobbit.dto.jobDto.JobDto;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CategoryDto {

    private Long categoryId;
    private String categoryName;
    private List<JobDto> jobs;

}
