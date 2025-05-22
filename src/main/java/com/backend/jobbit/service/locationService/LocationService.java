package com.backend.jobbit.service.locationService;

import com.backend.jobbit.dto.locationDto.LocationDto;
import com.backend.jobbit.dto.locationDto.LocationWithJobsDto;

import java.util.List;

public interface LocationService {

    LocationDto createLocation(LocationDto locationDto);
    LocationDto getLocationById(Long id);
    List<LocationDto> getAllLocations();
    LocationDto updateLocation(LocationDto locationDto, Long id);
    void deleteLocation(Long id);

}
