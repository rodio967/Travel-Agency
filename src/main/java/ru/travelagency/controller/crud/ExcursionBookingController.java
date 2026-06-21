package ru.travelagency.controller.crud;

import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import ru.travelagency.dto.ExcursionBookingDto;
import ru.travelagency.service.crud.ExcursionBookingService;

import static ru.travelagency.security.PermissionCodes.EXCURSION_BOOKING_READ;
import static ru.travelagency.security.PermissionCodes.EXCURSION_BOOKING_WRITE;

@RestController
@RequestMapping("/api/excursion-bookings")
public class ExcursionBookingController extends AbstractCrudController<ExcursionBookingDto, Long, ExcursionBookingService> {

    public ExcursionBookingController(ExcursionBookingService service) { super(service); }

    @Override
    @GetMapping
    @PreAuthorize("hasAuthority('" + EXCURSION_BOOKING_READ + "')")
    public Page<ExcursionBookingDto> list(Pageable pageable) { return super.list(pageable); }

    @Override
    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('" + EXCURSION_BOOKING_READ + "')")
    public ExcursionBookingDto getOne(@PathVariable Long id) { return super.getOne(id); }

    @Override
    @PostMapping
    @PreAuthorize("hasAuthority('" + EXCURSION_BOOKING_WRITE + "')")
    @ResponseStatus(HttpStatus.CREATED)
    public ExcursionBookingDto create(@RequestBody @Valid ExcursionBookingDto dto) { return super.create(dto); }

    @Override
    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('" + EXCURSION_BOOKING_WRITE + "')")
    public ExcursionBookingDto update(@PathVariable Long id, @RequestBody @Valid ExcursionBookingDto dto) { return super.update(id, dto); }

    @Override
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('" + EXCURSION_BOOKING_WRITE + "')")
    public ResponseEntity<Void> delete(@PathVariable Long id) { return super.delete(id); }
}
