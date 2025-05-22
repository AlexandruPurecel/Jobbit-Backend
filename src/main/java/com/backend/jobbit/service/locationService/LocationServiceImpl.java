package com.backend.jobbit.service.locationService;

import com.backend.jobbit.dto.jobDto.JobDto;
import com.backend.jobbit.dto.locationDto.LocationDto;
import com.backend.jobbit.dto.locationDto.LocationWithJobsDto;
import com.backend.jobbit.enums.Cities;
import com.backend.jobbit.exception.ResourceNotFoundException;
import com.backend.jobbit.persistence.model.Location;
import com.backend.jobbit.repository.LocationRepo;
import com.backend.jobbit.service.jobService.JobService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class LocationServiceImpl implements LocationService {

    private final LocationRepo locationRepo;
    private final JobService jobService;

    @Override
    public LocationDto createLocation(LocationDto dto) {
        Location location = new Location();
        location.setCity(dto.getCity());
        location.setStreetName(dto.getStreetName());
        location.setStreetNumber(dto.getStreetNumber());

        Location saved = locationRepo.save(location);
        return convertToDto(saved);
    }

    @Override
    public LocationDto getLocationById(Long id) {
        Location location = locationRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Location not found"));
        return convertToDto(location);
    }

    @Override
    public List<LocationDto> getAllLocations() {

        return Arrays.stream(Cities.values())
                .map(city -> {
                    LocationDto dto = new LocationDto();
                    dto.setCity(city);
                    return dto;
                })
                .collect(Collectors.toList());
    }

    @Override
    public LocationDto updateLocation(LocationDto dto, Long id) {
        Location location = locationRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Location not found"));

        location.setCity(dto.getCity());
        location.setStreetName(dto.getStreetName());
        location.setStreetNumber(dto.getStreetNumber());

        Location updated = locationRepo.save(location);
        return convertToDto(updated);
    }

    @Override
    public void deleteLocation(Long id) {
        Location location = locationRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Location not found"));
        locationRepo.delete(location);
    }

    private LocationDto convertToDto(Location location) {
        return new LocationDto(
                location.getLocationId(),
                location.getCity(),
                location.getStreetName(),
                location.getStreetNumber()
        );
    }
}
