package com.backend.jobbit.repository;

import com.backend.jobbit.persistence.model.Job;
import com.backend.jobbit.persistence.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface JobRepo extends JpaRepository<Job, Long> {

    List<Job> findByPostedBy(User user);
}
