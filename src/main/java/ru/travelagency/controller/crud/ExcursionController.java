package ru.travelagency.controller.crud;

import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import ru.travelagency.dto.ExcursionDto;
import ru.travelagency.service.crud.ExcursionService;

import static ru.travelagency.security.PermissionCodes.EXCURSION_READ;
import static ru.travelagency.security.PermissionCodes.EXCURSION_WRITE;

@RestController
@RequestMapping("/api/excursions")
public class ExcursionController extends AbstractCrudController<ExcursionDto, Long, ExcursionService> {

    public ExcursionController(ExcursionService service) { super(service); }

    @Override
    @GetMapping
    @PreAuthorize("hasAuthority('" + EXCURSION_READ + "')")
    public Page<ExcursionDto> list(Pageable pageable) { return super.list(pageable); }

    @Override
    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('" + EXCURSION_READ + "')")
    public ExcursionDto getOne(@PathVariable Long id) { return super.getOne(id); }

    @Override
    @PostMapping
    @PreAuthorize("hasAuthority('" + EXCURSION_WRITE + "')")
    @ResponseStatus(HttpStatus.CREATED)
    public ExcursionDto create(@RequestBody @Valid ExcursionDto dto) { return super.create(dto); }

    @Override
    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('" + EXCURSION_WRITE + "')")
    public ExcursionDto update(@PathVariable Long id, @RequestBody @Valid ExcursionDto dto) { return super.update(id, dto); }

    @Override
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('" + EXCURSION_WRITE + "')")
    public ResponseEntity<Void> delete(@PathVariable Long id) { return super.delete(id); }
}
