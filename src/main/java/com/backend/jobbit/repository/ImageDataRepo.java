package com.backend.jobbit.repository;

import com.backend.jobbit.persistence.ImageData;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ImageDataRepo extends JpaRepository<ImageData, Long> {
}
