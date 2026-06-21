package ru.travelagency.controller.crud;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import ru.travelagency.dto.AppUserDto;
import ru.travelagency.service.crud.AppUserService;

import static ru.travelagency.security.PermissionCodes.USER_MANAGE;


@RestController
@RequestMapping("/api/users")
public class AppUserController extends AbstractCrudController<AppUserDto, Long, AppUserService> {

    public AppUserController(AppUserService service) {
        super(service);
    }

    @Override
    @GetMapping
    @PreAuthorize("hasAuthority('" + USER_MANAGE + "')")
    public Page<AppUserDto> list(Pageable pageable) {
        return super.list(pageable);
    }

    @Override
    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('" + USER_MANAGE + "')")
    public AppUserDto getOne(@PathVariable Long id) {
        return super.getOne(id);
    }

    @Override
    @PostMapping
    @PreAuthorize("hasAuthority('" + USER_MANAGE + "')")
    @ResponseStatus(HttpStatus.CREATED)
    public AppUserDto create(@RequestBody @Valid AppUserDto dto) {
        return super.create(dto);
    }

    @Override
    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('" + USER_MANAGE + "')")
    public AppUserDto update(@PathVariable Long id, @RequestBody @Valid AppUserDto dto) {
        return super.update(id, dto);
    }

    @Override
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('" + USER_MANAGE + "')")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        return super.delete(id);
    }

    @PostMapping("/{id}/password")
    @PreAuthorize("hasAuthority('" + USER_MANAGE + "')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void changePassword(@PathVariable Long id,
                               @RequestBody @Valid ChangePasswordRequest request) {
        service.changePassword(id, request.newPassword());
    }

    public record ChangePasswordRequest(@NotBlank String newPassword) {}
}
