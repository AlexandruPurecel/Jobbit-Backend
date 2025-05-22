package com.backend.jobbit.dto.userDto;

import com.backend.jobbit.dto.jobDto.JobDto;
import lombok.*;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserWithAppliedJobsDto {

    private Long userId;
    private String firstName;
    private String lastName;
    private List<JobDto> appliedJobs;

}
