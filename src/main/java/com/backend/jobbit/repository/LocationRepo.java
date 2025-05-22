package com.backend.jobbit.repository;

import com.backend.jobbit.persistence.model.Location;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LocationRepo extends JpaRepository<Location,Long> {
}
