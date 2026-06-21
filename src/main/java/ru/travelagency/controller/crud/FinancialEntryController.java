package ru.travelagency.controller.crud;

import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import ru.travelagency.dto.FinancialEntryDto;
import ru.travelagency.service.crud.FinancialEntryService;

import static ru.travelagency.security.PermissionCodes.FINANCE_READ;
import static ru.travelagency.security.PermissionCodes.FINANCE_WRITE;

@RestController
@RequestMapping("/api/financial-entries")
public class FinancialEntryController extends AbstractCrudController<FinancialEntryDto, Long, FinancialEntryService> {

    public FinancialEntryController(FinancialEntryService service) { super(service); }

    @Override
    @GetMapping
    @PreAuthorize("hasAuthority('" + FINANCE_READ + "')")
    public Page<FinancialEntryDto> list(Pageable pageable) { return super.list(pageable); }

    @Override
    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('" + FINANCE_READ + "')")
    public FinancialEntryDto getOne(@PathVariable Long id) { return super.getOne(id); }

    @Override
    @PostMapping
    @PreAuthorize("hasAuthority('" + FINANCE_WRITE + "')")
    @ResponseStatus(HttpStatus.CREATED)
    public FinancialEntryDto create(@RequestBody @Valid FinancialEntryDto dto) { return super.create(dto); }

    @Override
    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('" + FINANCE_WRITE + "')")
    public FinancialEntryDto update(@PathVariable Long id, @RequestBody @Valid FinancialEntryDto dto) { return super.update(id, dto); }

    @Override
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('" + FINANCE_WRITE + "')")
    public ResponseEntity<Void> delete(@PathVariable Long id) { return super.delete(id); }
}
