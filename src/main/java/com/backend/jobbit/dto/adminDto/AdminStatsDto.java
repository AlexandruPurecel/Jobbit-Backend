package com.backend.jobbit.dto.adminDto;

import lombok.*;

import java.util.Map;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AdminStatsDto {
    private int totalUsers;
    private int totalJobs;
    private Map<String, Long> usersByRole;
}
