package ru.travelagency.controller.crud;

import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import ru.travelagency.dto.TouristGroupDto;
import ru.travelagency.service.crud.TouristGroupService;

import static ru.travelagency.security.PermissionCodes.GROUP_READ;
import static ru.travelagency.security.PermissionCodes.GROUP_WRITE;

@RestController
@RequestMapping("/api/groups")
public class TouristGroupController extends AbstractCrudController<TouristGroupDto, Long, TouristGroupService> {

    public TouristGroupController(TouristGroupService service) { super(service); }

    @Override
    @GetMapping
    @PreAuthorize("hasAuthority('" + GROUP_READ + "')")
    public Page<TouristGroupDto> list(Pageable pageable) { return super.list(pageable); }

    @Override
    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('" + GROUP_READ + "')")
    public TouristGroupDto getOne(@PathVariable Long id) { return super.getOne(id); }

    @Override
    @PostMapping
    @PreAuthorize("hasAuthority('" + GROUP_WRITE + "')")
    @ResponseStatus(HttpStatus.CREATED)
    public TouristGroupDto create(@RequestBody @Valid TouristGroupDto dto) { return super.create(dto); }

    @Override
    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('" + GROUP_WRITE + "')")
    public TouristGroupDto update(@PathVariable Long id, @RequestBody @Valid TouristGroupDto dto) { return super.update(id, dto); }

    @Override
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('" + GROUP_WRITE + "')")
    public ResponseEntity<Void> delete(@PathVariable Long id) { return super.delete(id); }
}
