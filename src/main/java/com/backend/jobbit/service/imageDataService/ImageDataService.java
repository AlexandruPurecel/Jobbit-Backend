package com.backend.jobbit.service.imageDataService;

import com.backend.jobbit.persistence.ImageData;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface ImageDataService {

    ImageData saveUserProfileImage(Long userId, MultipartFile file) throws IOException;
    ImageData saveJobImage(Long jobId, MultipartFile file) throws IOException;
    byte[] getImageData(Long id);
    void deleteImage(Long id);
}
