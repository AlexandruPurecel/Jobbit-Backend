package com.backend.jobbit.controller;

import com.backend.jobbit.service.imageDataService.ImageDataService;
import com.backend.jobbit.util.ImageUtil;
import lombok.*;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/api/images")
@RequiredArgsConstructor
@CrossOrigin("*")
public class ImageDataController {

    private final ImageDataService imageDataService;

    @PostMapping("/job/{jobId}/upload")
    public ResponseEntity<String> uploadJobImage(
            @PathVariable Long jobId,
            @RequestParam("image") MultipartFile file) {
        try {
            imageDataService.saveJobImage(jobId, file);
            return ResponseEntity.ok("Image uploaded successfully.");
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to upload image.");
        }
    }

    @PostMapping("/user/{userId}/upload")
    public ResponseEntity<String> uploadUserProfileImage(
            @PathVariable Long userId,
            @RequestParam("image") MultipartFile file) {
        try {
            imageDataService.saveUserProfileImage(userId, file);
            return ResponseEntity.ok("Profile image uploaded successfully.");
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to upload image.");
        }
    }

    @GetMapping("/{imageId}")
    public ResponseEntity<byte[]> getImage(@PathVariable Long imageId) {
        byte[] compressedImageData = imageDataService.getImageData(imageId);
        byte[] imageData = ImageUtil.decompressImage(compressedImageData);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.IMAGE_JPEG);
        return new ResponseEntity<>(imageData, headers, HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteImage(@PathVariable Long id) {
        try {
            imageDataService.deleteImage(id);
            return ResponseEntity.ok("Image deleted successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to delete image: " + e.getMessage());
        }
    }
}

