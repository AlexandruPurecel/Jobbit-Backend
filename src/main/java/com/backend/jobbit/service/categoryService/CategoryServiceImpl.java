package com.backend.jobbit.service.categoryService;

import com.backend.jobbit.dto.categoryDto.CategoryDto;
import com.backend.jobbit.dto.jobDto.JobDto;
import com.backend.jobbit.dto.locationDto.LocationDto;
import com.backend.jobbit.exception.ResourceNotFoundException;
import com.backend.jobbit.persistence.model.Category;
import com.backend.jobbit.persistence.model.Location;
import com.backend.jobbit.repository.CategoryRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepo categoryRepo;


    @Override
    public CategoryDto createCategory(CategoryDto categoryDto) {
        Category category = new Category();
        category.setName(categoryDto.getCategoryName());

        Category saved = categoryRepo.save(category);

        return convertToDto(saved);
    }

    @Override
    public CategoryDto getCategoryById(Long id) {
        Category category = categoryRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found"));
        return convertToDto(category);
    }

    @Override
    public List<CategoryDto> getAllCategories() {
        return categoryRepo.findAll().stream()
                .map(this::convertToDto)
                .toList();
    }

    @Override
    public CategoryDto updateCategory(CategoryDto categoryDto, Long id) {
        Category category = categoryRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found"));

        category.setName(categoryDto.getCategoryName());

        Category updated = categoryRepo.save(category);

        return convertToDto(updated);
    }

    @Override
    public void deleteCategory(Long id) {
        Category category = categoryRepo.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found"));

        categoryRepo.delete(category);
    }

    @Override
    public CategoryDto convertToDto(Category category) {
        List<JobDto> jobByCategory = category.getJobs() != null
                ? category.getJobs().stream()
                .map(job -> {

            Location loc = job.getLocation();
            LocationDto locationDto = new LocationDto(
                    loc.getLocationId(),
                    loc.getCity(),
                    loc.getStreetName(),
                    loc.getStreetNumber()
            );


            return new JobDto(
                    job.getJobId(),
                    job.getTitle(),
                    job.getDescription(),
                    job.getPrice(),
                    job.getCreatedAt(),
                    job.getPostedBy() != null ? job.getPostedBy().getUserId() : null,
                    job.getPostedBy() != null
                            ? job.getPostedBy().getFirstName() + " " + job.getPostedBy().getLastName()
                            : null,
                    category.getCategoryId(),
                    category.getName(),
                    locationDto
            );
        })
        .toList()
        : new ArrayList<>();


        return new CategoryDto(
                category.getCategoryId(),
                category.getName(),
                jobByCategory
        );
    }
}
