package com.backend.jobbit.service.userService;

import com.backend.jobbit.dto.jobDto.JobDto;
import com.backend.jobbit.dto.locationDto.LocationDto;
import com.backend.jobbit.dto.userDto.UserWithPostedJobsDto;
import com.backend.jobbit.exception.EmailAlreadyExistsException;
import com.backend.jobbit.exception.ResourceNotFoundException;
import com.backend.jobbit.dto.userDto.UserDto;
import com.backend.jobbit.dto.userDto.UserRegisterDto;
import com.backend.jobbit.persistence.ImageData;
import com.backend.jobbit.persistence.Role;
import com.backend.jobbit.persistence.model.Job;
import com.backend.jobbit.persistence.model.Location;
import com.backend.jobbit.persistence.model.User;
import com.backend.jobbit.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.backend.jobbit.repository.JobRepo;
import com.backend.jobbit.repository.RoleRepo;


import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepo userRepo;
    private final ImageDataRepo imageDataRepo;
    private final PasswordEncoder passwordEncoder;
    private final JobRepo jobRepo;
    private final RoleRepo roleRepo;

    @Override
    public UserDto createUser(UserRegisterDto userRegisterDto) {

        if(userRepo.existsByEmail(userRegisterDto.getEmail())){
            throw new EmailAlreadyExistsException("Email already in use");
        }
        User user = new User();
        user.setFirstName(userRegisterDto.getFirstName());
        user.setLastName(userRegisterDto.getLastName());
        user.setEmail(userRegisterDto.getEmail());
        String password = passwordEncoder.encode(userRegisterDto.getPassword());
        user.setPassword(password);
        Role userRole = roleRepo.findByName("USER")
                .orElseThrow(() -> new ResourceNotFoundException("Default USER role not found"));
        user.setRole(userRole);
        User savedUser = userRepo.save(user);

        return convertToDto(savedUser);


    }

    @Override
    public UserDto getUserById(Long id) {

        User user = userRepo.findById(id).orElseThrow( () ->
                new ResourceNotFoundException("User was not found"));

        return convertToDto(user);

    }

    @Override
    public List<UserDto> getAllUsers() {

        return userRepo.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Override
    public void deleteUser(Long id) {

        User user = userRepo.findById(id).orElseThrow( () ->
                new ResourceNotFoundException("User with this id was not found"));

        userRepo.delete(user);
    }

    @Override
    public UserDto updateUser(UserDto userDto, Long id) {

        User user = userRepo.findById(id).orElseThrow( () ->
                new ResourceNotFoundException("User not found"));

        user.setFirstName(userDto.getFirstName());
        user.setLastName(userDto.getLastName());
        user.setEmail(userDto.getEmail());
        user.setBio(userDto.getBio());

        if (userDto.getProfilePictureId() != null) {
            ImageData image = imageDataRepo.findById(userDto.getProfilePictureId())
                    .orElseThrow(() -> new ResourceNotFoundException("Image not found"));
            user.setProfileImage(image);
        }

        User savedUser = userRepo.save(user);

        return convertToDto(savedUser);
    }

    @Override
    public UserWithPostedJobsDto getUserPostedJobs(Long id) {
        User user = userRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));

        List<Job> postedJobs = jobRepo.findByPostedBy(user);

        List<JobDto> jobDtos = postedJobs.stream().map(job -> {
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
                    user.getUserId(),
                    user.getFirstName() + " " + user.getLastName(),
                    job.getCategory().getCategoryId(),
                    job.getCategory().getName(),
                    locationDto
            );
        }).toList();

        return new UserWithPostedJobsDto(
                user.getUserId(),
                user.getFirstName(),
                user.getLastName(),
                jobDtos
        );
    }

    @Override
    public UserDto assignRole(Long userId, String roleName) {
        if (!"ADMIN".equals(roleName) && !"USER".equals(roleName)) {
            throw new IllegalArgumentException("Invalid role. Only ADMIN and USER are supported.");
        }

        User user = userRepo.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        Role role = roleRepo.findByName(roleName)
                .orElseThrow(() -> new ResourceNotFoundException("Role not found: " + roleName));

        user.setRole(role);
        User savedUser = userRepo.save(user);
        return convertToDto(savedUser);
    }

    @Override
    public List<UserDto> getUsersByRole(String roleName) {
        Role role = roleRepo.findByName(roleName)
                .orElseThrow(() -> new ResourceNotFoundException("Role not found: " + roleName));

        return userRepo.findByRole(role).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }


    @Override
    public UserDto convertToDto(User user) {

        return new UserDto(
                user.getUserId(),
                user.getFirstName(),
                user.getLastName(),
                user.getEmail(),
                user.getBio(),
                user.getProfileImage() != null ? user.getProfileImage().getId() : null,
                user.getRole() != null ? user.getRole().getName() : null

        );

    }


}
