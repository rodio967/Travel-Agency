package ru.travelagency.controller.crud;

import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import ru.travelagency.dto.HotelBookingDto;
import ru.travelagency.service.crud.HotelBookingService;

import static ru.travelagency.security.PermissionCodes.HOTEL_BOOKING_READ;
import static ru.travelagency.security.PermissionCodes.HOTEL_BOOKING_WRITE;

@RestController
@RequestMapping("/api/hotel-bookings")
public class HotelBookingController extends AbstractCrudController<HotelBookingDto, Long, HotelBookingService> {

    public HotelBookingController(HotelBookingService service) { super(service); }

    @Override
    @GetMapping
    @PreAuthorize("hasAuthority('" + HOTEL_BOOKING_READ + "')")
    public Page<HotelBookingDto> list(Pageable pageable) { return super.list(pageable); }

    @Override
    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('" + HOTEL_BOOKING_READ + "')")
    public HotelBookingDto getOne(@PathVariable Long id) { return super.getOne(id); }

    @Override
    @PostMapping
    @PreAuthorize("hasAuthority('" + HOTEL_BOOKING_WRITE + "')")
    @ResponseStatus(HttpStatus.CREATED)
    public HotelBookingDto create(@RequestBody @Valid HotelBookingDto dto) { return super.create(dto); }

    @Override
    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('" + HOTEL_BOOKING_WRITE + "')")
    public HotelBookingDto update(@PathVariable Long id, @RequestBody @Valid HotelBookingDto dto) { return super.update(id, dto); }

    @Override
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('" + HOTEL_BOOKING_WRITE + "')")
    public ResponseEntity<Void> delete(@PathVariable Long id) { return super.delete(id); }
}
