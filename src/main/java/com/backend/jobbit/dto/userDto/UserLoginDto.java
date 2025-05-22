package com.backend.jobbit.dto.userDto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserLoginDto {

    private String email;
    private String password;
}
