package com.backend.jobbit.controller;

import com.backend.jobbit.dto.adminDto.AdminStatsDto;
import com.backend.jobbit.dto.jobDto.JobDto;
import com.backend.jobbit.dto.userDto.UserDto;
import com.backend.jobbit.dto.userDto.UserWithPostedJobsDto;
import com.backend.jobbit.exception.ResourceNotFoundException;
import com.backend.jobbit.persistence.model.User;
import com.backend.jobbit.repository.UserRepo;
import com.backend.jobbit.service.jobService.JobService;
import com.backend.jobbit.service.userService.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;


import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
@PreAuthorize("hasRole('ADMIN')")
public class AdminController {

    private final UserService userService;
    private final JobService jobService;

    @GetMapping("/users")
    public ResponseEntity<List<UserDto>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers());
    }

    @GetMapping("/users/admins")
    public ResponseEntity<List<UserDto>> getAllAdmins() {
        return ResponseEntity.ok(userService.getUsersByRole("ADMIN"));
    }

    @GetMapping("/users/regular")
    public ResponseEntity<List<UserDto>> getAllRegularUsers() {
        return ResponseEntity.ok(userService.getUsersByRole("USER"));
    }

    @PutMapping("/users/{userId}/promote")
    public ResponseEntity<UserDto> promoteToAdmin(@PathVariable Long userId) {
        UserDto updated = userService.assignRole(userId, "ADMIN");
        return ResponseEntity.ok(updated);
    }

    @PutMapping("/users/{userId}/demote")
    public ResponseEntity<UserDto> demoteToUser(@PathVariable Long userId) {
        UserDto updated = userService.assignRole(userId, "USER");
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/users/{userId}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long userId) {
        userService.deleteUser(userId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/jobs")
    public ResponseEntity<List<JobDto>> getAllJobs() {
        return ResponseEntity.ok(jobService.getAllJobs());
    }

    @DeleteMapping("/jobs/{jobId}")
    public ResponseEntity<Void> deleteJob(@PathVariable Long jobId) {
        jobService.deleteJob(jobId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/jobs/user/{userId}")
    public ResponseEntity<List<JobDto>> getJobsByUser(@PathVariable Long userId) {
        UserWithPostedJobsDto userWithJobs = userService.getUserPostedJobs(userId);
        return ResponseEntity.ok(userWithJobs.getPostedJobs());
    }

    @GetMapping("/stats")
    public ResponseEntity<AdminStatsDto> getAdminStats() {
        AdminStatsDto stats = new AdminStatsDto();
        stats.setTotalUsers(userService.getAllUsers().size());
        stats.setTotalJobs(jobService.getAllJobs().size());

        Map<String, Long> roleCount = new HashMap<>();
        roleCount.put("ADMIN", (long) userService.getUsersByRole("ADMIN").size());
        roleCount.put("USER", (long) userService.getUsersByRole("USER").size());
        stats.setUsersByRole(roleCount);

        return ResponseEntity.ok(stats);
    }
}
