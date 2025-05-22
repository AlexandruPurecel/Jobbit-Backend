//package com.backend.jobbit.service.jobApplicationService;
//
//import com.backend.jobbit.dto.jobAppDto.JobAppDto;
//import com.backend.jobbit.enums.ApplicationStatus;
//import com.backend.jobbit.exception.ResourceNotFoundException;
//import com.backend.jobbit.persistence.model.*;
//import com.backend.jobbit.persistence.model.JobApplication;
//import com.backend.jobbit.repository.*;
//import lombok.RequiredArgsConstructor;
//import org.springframework.stereotype.Service;
//
//@Service
//@RequiredArgsConstructor
//public class JobApplicationServiceImpl implements JobApplicationService{
//
//    private final JobApplicationRepo jobApplicationRepo;
//    private final JobRepo jobRepo;
//    private final UserRepo userRepo;
//
//    @Override
//    public JobAppDto applyToJob(JobAppDto jobAppDto) {
//        Job job = jobRepo.findById(jobAppDto.getJobId())
//                .orElseThrow(() -> new ResourceNotFoundException("Job not found"));
//
//        User user = userRepo.findById(jobAppDto.getUserId())
//                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
//
//        JobApplication application = new JobApplication();
//        application.setJob(job);
//        application.setUser(user);
//        application.setStatus(jobAppDto.getStatus() != null ? jobAppDto.getStatus() : ApplicationStatus.PENDING);
//
//        JobApplication savedApp = jobApplicationRepo.save(application);
//
//        return convertToDto(savedApp);
//    }
//
//    @Override
//    public void deleteApply(Long id) {
//        JobApplication app = jobApplicationRepo.findById(id)
//                .orElseThrow(() -> new ResourceNotFoundException("Application not found"));
//        jobApplicationRepo.delete(app);
//
//    }
//
//    @Override
//    public JobAppDto convertToDto(JobApplication application) {
//        return new JobAppDto(
//                application.getJobApplicationId(),
//                application.getStatus(),
//                application.getAppliedAt(),
//                application.getJob().getJobId(),
//                application.getJob().getTitle(),
//                application.getUser().getUserId(),
//                application.getUser().getFirstName(),
//                application.getUser().getLastName()
//        );
//    }
//}
