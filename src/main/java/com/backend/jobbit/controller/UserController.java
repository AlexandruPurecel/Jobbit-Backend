package com.backend.jobbit.controller;

import com.backend.jobbit.dto.userDto.*;
import com.backend.jobbit.service.userService.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("api/user")
@CrossOrigin("*")
public class UserController {

    private final UserService userService;

    @PostMapping("/new")
    public ResponseEntity<UserDto> createUser(@RequestBody UserRegisterDto userRegisterDto){

        UserDto newUser = userService.createUser(userRegisterDto);
        return new ResponseEntity<>(newUser, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDto> getUserById(@PathVariable Long id) {
        UserDto user = userService.getUserById(id);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<UserDto>> getAllUsers() {
        List<UserDto> users = userService.getAllUsers();
        return new ResponseEntity<>(users, HttpStatus.OK);
    }

    @PutMapping("update/{id}")
    public ResponseEntity<UserDto> updateUser(@RequestBody UserDto userDto, @PathVariable Long id) {
        UserDto updatedUser = userService.updateUser(userDto, id);
        return new ResponseEntity<>(updatedUser, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    @GetMapping("/postedJobs/{id}")
    public ResponseEntity<UserWithPostedJobsDto> getUserPostedJobs(@PathVariable Long id){
        UserWithPostedJobsDto user = userService.getUserPostedJobs(id);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }


}

