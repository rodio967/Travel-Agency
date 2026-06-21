package ru.travelagency.config;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ru.travelagency.domain.AppUser;
import ru.travelagency.domain.Role;
import ru.travelagency.repository.AppUserRepository;
import ru.travelagency.repository.RoleRepository;

import java.util.HashSet;
import java.util.Set;


@Slf4j
@Component
@Order(2)
public class DataInitializer implements CommandLineRunner {

    private final AppUserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final String adminUsername;
    private final String adminPassword;

    public DataInitializer(AppUserRepository userRepository,
                           RoleRepository roleRepository,
                           PasswordEncoder passwordEncoder,
                           @Value("${app.bootstrap.admin.username:admin}") String adminUsername,
                           @Value("${app.bootstrap.admin.password:admin}") String adminPassword) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.adminUsername = adminUsername;
        this.adminPassword = adminPassword;
    }

    @Override
    @Transactional
    public void run(String... args) {
        Role adminRole = roleRepository.findByName("ADMIN")
                .orElseThrow(() -> new IllegalStateException("Role ADMIN not found"));

        boolean adminExists = userRepository.findAll().stream()
                .anyMatch(u -> u.getRoles().stream()
                        .anyMatch(r -> "ADMIN".equals(r.getName())));

        if (adminExists) {
            log.info("ADMIN user already exist skipping bootstrap");
            return;
        }

        if (userRepository.existsByUsername(adminUsername)) {
            log.warn("Bootstrap skipped: username '{}' is already taken", adminUsername);
            return;
        }

        AppUser admin = new AppUser();
        admin.setUsername(adminUsername);
        admin.setPassword(passwordEncoder.encode(adminPassword));
        admin.setEnabled(true);
        Set<Role> roles = new HashSet<>();
        roles.add(adminRole);
        admin.setRoles(roles);
        userRepository.save(admin);

        log.info(" Bootstrap admin created: username='{}'", adminUsername);
        log.info(" Default password='{}'", adminPassword);
    }
}
