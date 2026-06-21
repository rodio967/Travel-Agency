package ru.travelagency.config;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import ru.travelagency.domain.Permission;
import ru.travelagency.domain.Role;
import ru.travelagency.repository.PermissionRepository;
import ru.travelagency.repository.RoleRepository;
import ru.travelagency.security.PermissionCodes;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static ru.travelagency.security.PermissionCodes.*;


@Slf4j
@Component
@Order(1)
public class PermissionSeeder implements CommandLineRunner {

    private final PermissionRepository permissionRepository;
    private final RoleRepository roleRepository;

    public PermissionSeeder(PermissionRepository permissionRepository,
                            RoleRepository roleRepository) {
        this.permissionRepository = permissionRepository;
        this.roleRepository = roleRepository;
    }

    @Override
    @Transactional
    public void run(String... args) {
        seedPermissions();
        seedSystemRoles();
    }

    private void seedPermissions() {
        int created = 0;
        for (var def : PermissionCodes.ALL) {
            if (def.code().startsWith("ROLE_")) {
                throw new IllegalStateException(
                        "Permission code must not start with ROLE_: " + def.code() + ". This conflicts with Spring Security role naming.");
            }

            if (permissionRepository.findByCode(def.code()).isEmpty()) {
                Permission p = new Permission();
                p.setCode(def.code());
                p.setCategory(def.category());
                p.setDescription(def.description());
                permissionRepository.save(p);
                created++;
            }
        }

        if (created > 0) {
            log.info("Seeded {} new permissions", created);
        }
    }


    private void seedSystemRoles() {
        upsertSystemRole("ADMIN", "Администратор: полный доступ",
                allPermissionCodes());

        upsertSystemRole("MANAGER", "Менеджер: ведёт клиентов и сделки",
                List.of(
                        TOURIST_READ, TOURIST_WRITE,
                        GROUP_READ, GROUP_WRITE,
                        VISA_READ, VISA_WRITE,
                        HOTEL_READ, HOTEL_WRITE,
                        HOTEL_BOOKING_READ, HOTEL_BOOKING_WRITE,
                        FLIGHT_READ, FLIGHT_WRITE,
                        CARGO_READ, CARGO_WRITE,
                        AGENCY_READ, AGENCY_WRITE,
                        EXCURSION_READ, EXCURSION_WRITE,
                        EXCURSION_BOOKING_READ, EXCURSION_BOOKING_WRITE,
                        FINANCE_READ, FINANCE_WRITE,
                        REPORT_VIEW));

        upsertSystemRole("OPERATOR", "Оператор: только просмотр и отчёты",
                List.of(
                        TOURIST_READ, GROUP_READ, VISA_READ,
                        HOTEL_READ, HOTEL_BOOKING_READ,
                        FLIGHT_READ, CARGO_READ,
                        AGENCY_READ, EXCURSION_READ, EXCURSION_BOOKING_READ,
                        FINANCE_READ,
                        REPORT_VIEW));
    }


    private void upsertSystemRole(String name, String description, List<String> permissionCodes) {
        Role role = roleRepository.findByName(name).orElseGet(() -> {
            Role r = new Role();
            r.setName(name);
            r.setSystemic(true);
            log.info("Creating system role: {}", name);
            return r;
        });

        role.setDescription(description);
        role.setSystemic(true);

        Set<Permission> perms = new HashSet<>(role.getPermissions());
        for (String code : permissionCodes) {
            permissionRepository.findByCode(code).ifPresent(perms::add);
        }

        role.setPermissions(perms);
        roleRepository.save(role);
    }

    private List<String> allPermissionCodes() {
        return PermissionCodes.ALL.stream().map(PermissionCodes.PermissionDef::code).toList();
    }
}
