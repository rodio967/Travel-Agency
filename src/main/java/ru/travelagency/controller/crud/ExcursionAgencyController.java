package ru.travelagency.controller.crud;

import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import ru.travelagency.dto.core.ExcursionAgencyDto;
import ru.travelagency.service.crud.ExcursionAgencyService;

import static ru.travelagency.security.PermissionCodes.AGENCY_READ;
import static ru.travelagency.security.PermissionCodes.AGENCY_WRITE;

@RestController
@RequestMapping("/api/excursion-agencies")
public class ExcursionAgencyController extends AbstractCrudController<ExcursionAgencyDto, Long, ExcursionAgencyService> {

    public ExcursionAgencyController(ExcursionAgencyService service) { super(service); }

    @Override
    @GetMapping
    @PreAuthorize("hasAuthority('" + AGENCY_READ + "')")
    public Page<ExcursionAgencyDto> list(Pageable pageable) { return super.list(pageable); }

    @Override
    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('" + AGENCY_READ + "')")
    public ExcursionAgencyDto getOne(@PathVariable Long id) { return super.getOne(id); }

    @Override
    @PostMapping
    @PreAuthorize("hasAuthority('" + AGENCY_WRITE + "')")
    @ResponseStatus(HttpStatus.CREATED)
    public ExcursionAgencyDto create(@RequestBody @Valid ExcursionAgencyDto dto) { return super.create(dto); }

    @Override
    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('" + AGENCY_WRITE + "')")
    public ExcursionAgencyDto update(@PathVariable Long id, @RequestBody @Valid ExcursionAgencyDto dto) { return super.update(id, dto); }

    @Override
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('" + AGENCY_WRITE + "')")
    public ResponseEntity<Void> delete(@PathVariable Long id) { return super.delete(id); }
}
