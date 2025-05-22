package com.backend.jobbit.repository;


import com.backend.jobbit.persistence.Role;
import com.backend.jobbit.persistence.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepo extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);
    Boolean existsByEmail(String email);
    List<User> findByRole(Role role);

}
