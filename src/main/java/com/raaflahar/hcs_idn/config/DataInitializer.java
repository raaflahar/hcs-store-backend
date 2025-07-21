package com.raaflahar.hcs_idn.config;

import com.raaflahar.hcs_idn.constant.ERole;
import com.raaflahar.hcs_idn.entity.Role;
import com.raaflahar.hcs_idn.entity.User;
import com.raaflahar.hcs_idn.repository.RoleRepository;
import com.raaflahar.hcs_idn.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.Date;

@Component
@RequiredArgsConstructor
@Slf4j
public class DataInitializer implements CommandLineRunner {

    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        initializeRoles();
        initializeAdminUser();
    }

    private void initializeRoles() {
        if (roleRepository.findByName(ERole.ROLE_ADMIN).isEmpty()) {
            roleRepository.save(Role.builder().name(ERole.ROLE_ADMIN).build());
            log.info("ROLE_ADMIN initialized");
        }
        if (roleRepository.findByName(ERole.ROLE_USER).isEmpty()) {
            roleRepository.save(Role.builder().name(ERole.ROLE_USER).build());
            log.info("ROLE_USER initialized");
        }
        if (roleRepository.findByName(ERole.ROLE_CUSTOMER).isEmpty()) {
            roleRepository.save(Role.builder().name(ERole.ROLE_CUSTOMER).build());
            log.info("ROLE_CUSTOMER initialized");
        }
    }

    private void initializeAdminUser() {
        if (userRepository.findByUsername("admin").isEmpty()) {
            Role adminRole = roleRepository.findByName(ERole.ROLE_ADMIN)
                    .orElseThrow(() -> new RuntimeException("Error: Role is not found."));

            User adminUser = User.builder()
                    .username("admin")
                    .fullName("Administrator")
                    .email("admin@hcs.co.id")
                    .password(passwordEncoder.encode("password"))
                    .roles(Collections.singletonList(adminRole))
                    .createdAt(new Date())
                    .isVerified(true)
                    .build();

            userRepository.save(adminUser);
            log.info("Admin user initialized with username 'admin' and password 'password'");
        }
    }
}
