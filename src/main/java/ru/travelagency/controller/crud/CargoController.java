package ru.travelagency.controller.crud;

import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import ru.travelagency.dto.CargoDto;
import ru.travelagency.service.crud.CargoService;

import static ru.travelagency.security.PermissionCodes.CARGO_READ;
import static ru.travelagency.security.PermissionCodes.CARGO_WRITE;

@RestController
@RequestMapping("/api/cargo")
public class CargoController extends AbstractCrudController<CargoDto, Long, CargoService> {

    public CargoController(CargoService service) { super(service); }

    @Override
    @GetMapping
    @PreAuthorize("hasAuthority('" + CARGO_READ + "')")
    public Page<CargoDto> list(Pageable pageable) { return super.list(pageable); }

    @Override
    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('" + CARGO_READ + "')")
    public CargoDto getOne(@PathVariable Long id) { return super.getOne(id); }

    @Override
    @PostMapping
    @PreAuthorize("hasAuthority('" + CARGO_WRITE + "')")
    @ResponseStatus(HttpStatus.CREATED)
    public CargoDto create(@RequestBody @Valid CargoDto dto) { return super.create(dto); }

    @Override
    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('" + CARGO_WRITE + "')")
    public CargoDto update(@PathVariable Long id, @RequestBody @Valid CargoDto dto) { return super.update(id, dto); }

    @Override
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('" + CARGO_WRITE + "')")
    public ResponseEntity<Void> delete(@PathVariable Long id) { return super.delete(id); }
}
