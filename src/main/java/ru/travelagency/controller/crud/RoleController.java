package ru.travelagency.controller.crud;

import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import ru.travelagency.dto.PermissionDto;
import ru.travelagency.dto.RoleDto;
import ru.travelagency.service.crud.RoleService;

import java.util.List;

import static ru.travelagency.security.PermissionCodes.ROLES_MANAGE;

@RestController
@RequestMapping("/api/roles")
@PreAuthorize("hasAuthority('" + ROLES_MANAGE + "')")
public class RoleController {

    private final RoleService service;

    public RoleController(RoleService service) {
        this.service = service;
    }

    @GetMapping
    public Page<RoleDto> list(Pageable pageable) {
        return service.findAll(pageable);
    }

    @GetMapping("/{id}")
    public RoleDto getOne(@PathVariable Long id) {
        return service.findById(id);
    }

    @GetMapping("/permissions")
    public List<PermissionDto> allPermissions() {
        return service.allPermissions();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public RoleDto create(@RequestBody @Valid RoleDto dto) {
        return service.create(dto);
    }

    @PutMapping("/{id}")
    public RoleDto update(@PathVariable Long id, @RequestBody @Valid RoleDto dto) {
        return service.update(id, dto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
