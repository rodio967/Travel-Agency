package ru.travelagency.controller.crud;

import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import ru.travelagency.dto.core.FlightDto;
import ru.travelagency.service.crud.FlightService;

import static ru.travelagency.security.PermissionCodes.FLIGHT_READ;
import static ru.travelagency.security.PermissionCodes.FLIGHT_WRITE;

@RestController
@RequestMapping("/api/flights")
public class FlightController extends AbstractCrudController<FlightDto, Long, FlightService> {

    public FlightController(FlightService service) { super(service); }

    @Override
    @GetMapping
    @PreAuthorize("hasAuthority('" + FLIGHT_READ + "')")
    public Page<FlightDto> list(Pageable pageable) { return super.list(pageable); }

    @Override
    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('" + FLIGHT_READ + "')")
    public FlightDto getOne(@PathVariable Long id) { return super.getOne(id); }

    @Override
    @PostMapping
    @PreAuthorize("hasAuthority('" + FLIGHT_WRITE + "')")
    @ResponseStatus(HttpStatus.CREATED)
    public FlightDto create(@RequestBody @Valid FlightDto dto) { return super.create(dto); }

    @Override
    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('" + FLIGHT_WRITE + "')")
    public FlightDto update(@PathVariable Long id, @RequestBody @Valid FlightDto dto) { return super.update(id, dto); }

    @Override
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('" + FLIGHT_WRITE + "')")
    public ResponseEntity<Void> delete(@PathVariable Long id) { return super.delete(id); }
}
