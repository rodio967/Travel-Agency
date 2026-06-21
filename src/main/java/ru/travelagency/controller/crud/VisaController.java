package ru.travelagency.controller.crud;

import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import ru.travelagency.dto.VisaDto;
import ru.travelagency.service.crud.VisaService;

import static ru.travelagency.security.PermissionCodes.VISA_READ;
import static ru.travelagency.security.PermissionCodes.VISA_WRITE;

@RestController
@RequestMapping("/api/visas")
public class VisaController extends AbstractCrudController<VisaDto, Long, VisaService> {

    public VisaController(VisaService service) { super(service); }

    @Override
    @GetMapping
    @PreAuthorize("hasAuthority('" + VISA_READ + "')")
    public Page<VisaDto> list(Pageable pageable) { return super.list(pageable); }

    @Override
    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('" + VISA_READ + "')")
    public VisaDto getOne(@PathVariable Long id) { return super.getOne(id); }

    @Override
    @PostMapping
    @PreAuthorize("hasAuthority('" + VISA_WRITE + "')")
    @ResponseStatus(HttpStatus.CREATED)
    public VisaDto create(@RequestBody @Valid VisaDto dto) { return super.create(dto); }

    @Override
    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('" + VISA_WRITE + "')")
    public VisaDto update(@PathVariable Long id, @RequestBody @Valid VisaDto dto) { return super.update(id, dto); }

    @Override
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('" + VISA_WRITE + "')")
    public ResponseEntity<Void> delete(@PathVariable Long id) { return super.delete(id); }
}
