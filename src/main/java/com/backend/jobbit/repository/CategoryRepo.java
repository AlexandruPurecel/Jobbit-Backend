package com.backend.jobbit.repository;

import com.backend.jobbit.persistence.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepo extends JpaRepository<Category, Long> {
}
