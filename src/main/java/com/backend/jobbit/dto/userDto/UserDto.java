package com.backend.jobbit.dto.userDto;

import lombok.*;



@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {

    private Long userId;
    private String firstName;
    private String lastName;
    private String email;
    private String bio;
    private Long profilePictureId;
    private String roleName;



}
