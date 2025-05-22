package com.backend.jobbit.dto.userDto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserRegisterDto {

    private String firstName;
    private String lastName;
    private String email;
    private String password;
}
