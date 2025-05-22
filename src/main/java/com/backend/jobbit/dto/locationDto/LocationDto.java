package com.backend.jobbit.dto.locationDto;

import com.backend.jobbit.dto.jobDto.JobDto;
import com.backend.jobbit.enums.Cities;

import lombok.*;

import java.util.List;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LocationDto {

    private Long locationId;
    private Cities city;
    private String streetName;
    private Long streetNumber;
}