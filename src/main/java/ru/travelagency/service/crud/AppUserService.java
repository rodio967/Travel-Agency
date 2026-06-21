package ru.travelagency.service.crud;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.travelagency.domain.AppUser;
import ru.travelagency.domain.Role;
import ru.travelagency.dto.core.AppUserDto;
import ru.travelagency.exception.BusinessRuleException;
import ru.travelagency.exception.NotFoundException;
import ru.travelagency.mapper.AppUserMapper;
import ru.travelagency.repository.AppUserRepository;
import ru.travelagency.repository.RoleRepository;

import java.util.HashSet;
import java.util.Set;


@Service
public class AppUserService extends AbstractCrudService<AppUser, Long, AppUserDto> {

    private final AppUserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    public AppUserService(AppUserRepository repository,
                          AppUserMapper mapper,
                          RoleRepository roleRepository,
                          PasswordEncoder passwordEncoder) {
        super(repository, mapper, AppUser.class);
        this.userRepository = repository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    protected void beforeCreate(AppUser entity, AppUserDto dto) {
        if (userRepository.existsByUsername(dto.getUsername())) {
            throw new BusinessRuleException("Username already taken: " + dto.getUsername());
        }
        if (dto.getPassword() == null || dto.getPassword().isBlank()) {
            throw new BusinessRuleException("Password is required");
        }
        entity.setPassword(passwordEncoder.encode(dto.getPassword()));
        entity.setRoles(loadRoles(dto.getRoleIds()));
    }

    @Override
    protected void beforeUpdate(AppUser entity, AppUserDto dto) {
        if (dto.getRoleIds() != null) {
            entity.setRoles(loadRoles(dto.getRoleIds()));
        }
    }

    @Transactional
    public void changePassword(Long userId, String newPassword) {
        if (newPassword == null || newPassword.isBlank()) {
            throw new BusinessRuleException("Password cannot be empty");
        }

        AppUser u = userRepository.findById(userId)
                .orElseThrow(() -> NotFoundException.of(AppUser.class, userId));

        u.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(u);
    }

    private Set<Role> loadRoles(Set<Long> ids) {
        if (ids == null || ids.isEmpty()) return new HashSet<>();
        Set<Role> roles = new HashSet<>(roleRepository.findAllById(ids));
        if (roles.size() != ids.size()) {
            ids.stream()
                    .filter(id -> roles.stream().noneMatch(r -> r.getId().equals(id)))
                    .findFirst()
                    .ifPresent(missing -> {
                        throw NotFoundException.of(Role.class, missing);
                    });
        }
        return roles;
    }
}
