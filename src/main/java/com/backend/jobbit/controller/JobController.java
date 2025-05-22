package com.backend.jobbit.controller;

import com.backend.jobbit.config.security.JwtUtil;
import com.backend.jobbit.dto.jobDto.*;
import com.backend.jobbit.service.jobService.JobService;
import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("api/job")
@CrossOrigin("*")
public class JobController {

    private final JobService jobService;

    private final JwtUtil jwtUtil;


    @PostMapping("/new")
    public ResponseEntity<JobDto> createJob(@RequestBody JobDto jobDto) {
        JobDto createdJob = jobService.createJob(jobDto);
        return new ResponseEntity<>(createdJob, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<JobDto> getJobById(@PathVariable Long id) {
        JobDto job = jobService.getJobById(id);
        return new ResponseEntity<>(job, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<JobDto>> getAllJobs() {
        List<JobDto> jobs = jobService.getAllJobs();
        return new ResponseEntity<>(jobs, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> updateJob(@PathVariable Long id, @RequestBody JobDto jobDto,
                                       @RequestHeader("Authorization") String token) {
        try {
            String jwtToken = token.replace("Bearer ", "");
            Claims claims = jwtUtil.extractAllClaims(jwtToken);
            Long currentUserId = claims.get("userId", Long.class);

            JobDto existingJob = jobService.getJobById(id);
            if (existingJob == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("Job not found with id: " + id);
            }

            if (!existingJob.getPostedById().equals(currentUserId)) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                        .body("You do not have permission to edit this job");
            }

            JobDto updatedJob = jobService.updateJob(jobDto, id);
            return ResponseEntity.ok(updatedJob);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to update job: " + e.getMessage());
        }
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteJob(@PathVariable Long id) {
        jobService.deleteJob(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/image/{id}")
    public ResponseEntity<JobWithImagesDto> getJobWithImage(@PathVariable Long id){
        JobWithImagesDto job = jobService.getJobWithImagesById(id);
        return new ResponseEntity<>(job, HttpStatus.OK);
    }
}
