package com.backend.jobbit.controller;

import com.backend.jobbit.dto.locationDto.LocationDto;
import com.backend.jobbit.service.locationService.LocationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/location")
@RequiredArgsConstructor
@CrossOrigin("*")
public class LocationController {

    private final LocationService locationService;

    @PostMapping("/new")
    public ResponseEntity<LocationDto> createLocation(@RequestBody LocationDto locationDto) {
        LocationDto created = locationService.createLocation(locationDto);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<LocationDto> getLocationById(@PathVariable Long id) {
        LocationDto dto = locationService.getLocationById(id);
        return new ResponseEntity<>(dto, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<List<LocationDto>> getAllLocations() {
        List<LocationDto> all = locationService.getAllLocations();
        return new ResponseEntity<>(all, HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<LocationDto> updateLocation(@RequestBody LocationDto locationDto, @PathVariable Long id) {
        LocationDto updated = locationService.updateLocation(locationDto, id);
        return new ResponseEntity<>(updated, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteLocation(@PathVariable Long id) {
        locationService.deleteLocation(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
