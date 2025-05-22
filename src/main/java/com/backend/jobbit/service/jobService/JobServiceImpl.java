package com.backend.jobbit.service.jobService;

import com.backend.jobbit.dto.imageDto.ImageDto;
import com.backend.jobbit.dto.jobDto.JobWithImagesDto;
import com.backend.jobbit.exception.ResourceNotFoundException;
import com.backend.jobbit.dto.jobDto.JobDto;
import com.backend.jobbit.persistence.model.*;
import com.backend.jobbit.util.ImageUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.backend.jobbit.repository.*;
import com.backend.jobbit.dto.locationDto.LocationDto;

import java.util.List;

@Service
@RequiredArgsConstructor
public class JobServiceImpl implements JobService{

    private final JobRepo jobRepo;
    private final UserRepo userRepo;
    private final CategoryRepo categoryRepo;
    private final LocationRepo locationRepo;

    @Override
    public JobDto createJob(JobDto jobDto) {
        Job job = new Job();

        job.setTitle(jobDto.getTitle());
        job.setDescription(jobDto.getDescription());
        job.setPrice(jobDto.getPrice());

        if (jobDto.getPostedById() != null) {
            User user = userRepo.findById(jobDto.getPostedById())
                    .orElseThrow(() -> new ResourceNotFoundException("User not found"));
            job.setPostedBy(user);
        }

        if (jobDto.getCategoryId() != null) {
            Category category = categoryRepo.findById(jobDto.getCategoryId())
                    .orElseThrow(() -> new ResourceNotFoundException("Category not found"));
            job.setCategory(category);
        }

        LocationDto locDto = jobDto.getLocationDto();
        if (locDto == null) {
            throw new IllegalArgumentException("Location is required when creating a job.");
        }

        Location newLocation = new Location();
        newLocation.setCity(locDto.getCity());
        newLocation.setStreetName(locDto.getStreetName());
        newLocation.setStreetNumber(locDto.getStreetNumber());

        Location savedLocation = locationRepo.save(newLocation);
        job.setLocation(savedLocation);


        Job savedJob = jobRepo.save(job);

        return convertToDto(savedJob);
    }

    @Override
    public JobDto getJobById(Long id) {
        Job job = jobRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Job not found"));
        return convertToDto(job);
    }

    @Override
    public List<JobDto> getAllJobs() {
        return jobRepo.findAll().stream()
                .map(this::convertToDto)
                .toList();
    }

    @Override
    public JobDto updateJob(JobDto jobDto, Long id) {
        Job job = jobRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Job not found"));

        job.setTitle(jobDto.getTitle());
        job.setDescription(jobDto.getDescription());
        job.setPrice(jobDto.getPrice());

        if (jobDto.getCategoryId() != null) {
            Category category = categoryRepo.findById(jobDto.getCategoryId())
                    .orElseThrow(() -> new ResourceNotFoundException("Category not found"));
            job.setCategory(category);
        }

        LocationDto locDto = jobDto.getLocationDto();
        if (locDto != null) {
            Location locationToUpdate = job.getLocation() != null ? job.getLocation() : new Location();
            locationToUpdate.setCity(locDto.getCity());
            locationToUpdate.setStreetName(locDto.getStreetName());
            locationToUpdate.setStreetNumber(locDto.getStreetNumber());

            Location savedLocation = locationRepo.save(locationToUpdate);
            job.setLocation(savedLocation);
        }

        Job updatedJob = jobRepo.save(job);
        return convertToDto(updatedJob);
    }

    @Override
    public void deleteJob(Long id) {
        Job job = jobRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Job not found"));
        jobRepo.delete(job);

    }

    @Override
    public JobWithImagesDto getJobWithImagesById(Long id) {
        Job job = jobRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Job not found"));

        List<ImageDto> images = job.getImages().stream()
                .map(image -> new ImageDto(
                        image.getId(),
                        image.getName(),
                        image.getType(),
                        ImageUtil.decompressImage(image.getImageData())
                ))
                .toList();

        return new JobWithImagesDto(
                job.getJobId(),
                images
        );
    }

    @Override
    public JobDto convertToDto(Job job) {

        Location location = job.getLocation();
        LocationDto locationDto = new LocationDto(
                location.getLocationId(),
                location.getCity(),
                location.getStreetName(),
                location.getStreetNumber()
        );


        return new JobDto(
                job.getJobId(),
                job.getTitle(),
                job.getDescription(),
                job.getPrice(),
                job.getCreatedAt(),
                job.getPostedBy() != null ? job.getPostedBy().getUserId() : null,
                job.getPostedBy() != null
                        ? job.getPostedBy().getFirstName() + " " + job.getPostedBy().getLastName()
                        : null,
                job.getCategory() != null ? job.getCategory().getCategoryId() : null,
                job.getCategory() != null ? job.getCategory().getName() : null,
                locationDto
        );
    }
}
