package com.backend.jobbit.repository;

import com.backend.jobbit.persistence.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepo extends JpaRepository<Role, Long> {

    Optional<Role> findByName(String name);
    boolean existsByName(String name);
}
