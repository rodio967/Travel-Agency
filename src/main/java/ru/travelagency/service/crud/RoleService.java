package ru.travelagency.service.crud;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.travelagency.domain.Permission;
import ru.travelagency.domain.Role;
import ru.travelagency.dto.PermissionDto;
import ru.travelagency.dto.RoleDto;
import ru.travelagency.exception.BusinessRuleException;
import ru.travelagency.exception.NotFoundException;
import ru.travelagency.repository.PermissionRepository;
import ru.travelagency.repository.RoleRepository;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;


@Service
public class RoleService {

    private final RoleRepository roleRepository;
    private final PermissionRepository permissionRepository;

    public RoleService(RoleRepository roleRepository,
                       PermissionRepository permissionRepository) {
        this.roleRepository = roleRepository;
        this.permissionRepository = permissionRepository;
    }

    @Transactional(readOnly = true)
    public Page<RoleDto> findAll(Pageable pageable) {
        return roleRepository.findAll(pageable).map(this::toDto);
    }

    @Transactional(readOnly = true)
    public RoleDto findById(Long id) {
        return toDto(loadRole(id));
    }

    @Transactional(readOnly = true)
    public List<PermissionDto> allPermissions() {
        return permissionRepository.findAll().stream()
                .map(p -> new PermissionDto(p.getId(), p.getCode(), p.getCategory(), p.getDescription()))
                .toList();
    }

    @Transactional
    public RoleDto create(RoleDto dto) {
        if (roleRepository.existsByName(dto.getName())) {
            throw new BusinessRuleException("Role already exists: " + dto.getName());
        }
        Role role = new Role();
        role.setName(dto.getName());
        role.setDescription(dto.getDescription());
        role.setSystemic(false);
        role.setPermissions(loadPermissions(dto.getPermissionIds()));
        return toDto(roleRepository.save(role));
    }

    @Transactional
    public RoleDto update(Long id, RoleDto dto) {
        Role role = loadRole(id);

        if (!role.isSystemic() && dto.getName() != null) {
            role.setName(dto.getName());
        }
        if (dto.getDescription() != null) {
            role.setDescription(dto.getDescription());
        }
        if (dto.getPermissionIds() != null) {
            role.setPermissions(loadPermissions(dto.getPermissionIds()));
        }
        return toDto(roleRepository.save(role));
    }

    @Transactional
    public void delete(Long id) {
        Role role = loadRole(id);
        if (role.isSystemic()) {
            throw new BusinessRuleException("System role cannot be deleted: " + role.getName());
        }
        roleRepository.delete(role);
    }

    private Role loadRole(Long id) {
        return roleRepository.findById(id)
                .orElseThrow(() -> NotFoundException.of(Role.class, id));
    }

    private Set<Permission> loadPermissions(Set<Long> ids) {
        if (ids == null || ids.isEmpty()) return new HashSet<>();

        Set<Permission> perms = new HashSet<>(permissionRepository.findAllById(ids));
        if (perms.size() != ids.size()) {
            ids.stream()
                    .filter(id -> perms.stream().noneMatch(p -> p.getId().equals(id)))
                    .findFirst()
                    .ifPresent(missing -> {
                        throw NotFoundException.of(Permission.class, missing);
                    });
        }
        return perms;
    }

    private RoleDto toDto(Role role) {
        return RoleDto.builder()
                .id(role.getId())
                .name(role.getName())
                .description(role.getDescription())
                .systemic(role.isSystemic())
                .permissionIds(role.getPermissions().stream()
                        .map(Permission::getId)
                        .collect(Collectors.toCollection(HashSet::new)))
                .permissionCodes(role.getPermissions().stream()
                        .map(Permission::getCode)
                        .collect(Collectors.toCollection(HashSet::new)))
                .build();
    }
}
