package com.backend.jobbit.config;

import com.backend.jobbit.persistence.Role;
import com.backend.jobbit.persistence.model.User;
import com.backend.jobbit.repository.RoleRepo;
import com.backend.jobbit.repository.UserRepo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class DataInitializer implements CommandLineRunner {

    private final RoleRepo roleRepo;
    private final UserRepo userRepo;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {

        if (!roleRepo.existsByName("USER")) {
            Role userRole = new Role();
            userRole.setName("USER");
            roleRepo.save(userRole);
            log.info("Created USER role");
        }

        if (!roleRepo.existsByName("ADMIN")) {
            Role adminRole = new Role();
            adminRole.setName("ADMIN");
            roleRepo.save(adminRole);
            log.info("Created ADMIN role");
        }

        Role adminRole = roleRepo.findByName("ADMIN").orElse(null);
        if (adminRole != null && userRepo.findByRole(adminRole).isEmpty()) {
            User adminUser = new User();
            adminUser.setFirstName("Admin");
            adminUser.setLastName("User");
            adminUser.setEmail("admin@admin.com");
            adminUser.setPassword(passwordEncoder.encode("1234"));
            adminUser.setRole(adminRole);
            userRepo.save(adminUser);
            log.info("Created default admin: admin@admin.com / 1234");
        }

    }
}
