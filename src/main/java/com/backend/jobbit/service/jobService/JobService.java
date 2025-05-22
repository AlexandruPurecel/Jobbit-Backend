package com.backend.jobbit.service.jobService;

import com.backend.jobbit.dto.jobDto.JobDto;
import com.backend.jobbit.dto.jobDto.JobWithImagesDto;
import com.backend.jobbit.persistence.model.Job;

import java.util.List;

public interface JobService {

    JobDto createJob(JobDto jobDto);
    JobDto getJobById(Long id);
    List<JobDto> getAllJobs();
    JobDto updateJob(JobDto jobDto, Long id);
    void deleteJob(Long id);
    JobDto convertToDto(Job job);
    JobWithImagesDto getJobWithImagesById(Long id);

}
