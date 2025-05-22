package com.backend.jobbit.service.userService;

import com.backend.jobbit.dto.userDto.*;
import com.backend.jobbit.persistence.model.User;

import java.util.List;

public interface UserService {

    UserDto createUser(UserRegisterDto userRegisterDto);
    UserDto getUserById(Long id);
    List<UserDto> getAllUsers();
    void deleteUser(Long id);
    UserDto updateUser(UserDto userDto, Long id);
    UserDto convertToDto(User user);
    UserWithPostedJobsDto getUserPostedJobs(Long id);
    UserDto assignRole(Long userId, String roleName);
    List<UserDto> getUsersByRole(String roleName);


}
