package com.backend.jobbit.service.imageDataService;

import com.backend.jobbit.exception.ResourceNotFoundException;
import com.backend.jobbit.persistence.ImageData;
import com.backend.jobbit.persistence.model.*;
import com.backend.jobbit.repository.*;
import com.backend.jobbit.util.ImageUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class ImageDataServiceImpl implements ImageDataService{

    private final ImageDataRepo imageDataRepo;
    private final UserRepo userRepo;
    private final JobRepo jobRepo;

    @Override
    public ImageData saveUserProfileImage(Long userId, MultipartFile file) throws IOException {

        User user = userRepo.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));



        ImageData image = new ImageData();
        image.setName(file.getOriginalFilename());
        image.setType(file.getContentType());
        image.setImageData(ImageUtil.compressImage(file.getBytes()));
        image.setUser(user);

        imageDataRepo.save(image);
        user.setProfileImage(image);
        userRepo.save(user);
        return image;
    }



    @Override
    public ImageData saveJobImage(Long jobId, MultipartFile file) throws IOException {

        Job job = jobRepo.findById(jobId)
                .orElseThrow(() -> new ResourceNotFoundException("Job not found"));

        ImageData image = new ImageData();
        image.setName(file.getOriginalFilename());
        image.setType(file.getContentType());
        image.setImageData(ImageUtil.compressImage(file.getBytes()));
        image.setJob(job);

        return imageDataRepo.save(image);

    }

    @Override
    public byte[] getImageData(Long id) {
        return imageDataRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Image not found"))
                .getImageData();
    }

    @Override
    public void deleteImage(Long imageId) {
        ImageData image = imageDataRepo.findById(imageId)
                .orElseThrow(() -> new ResourceNotFoundException("Image not found"));

        imageDataRepo.delete(image);
    }
}
