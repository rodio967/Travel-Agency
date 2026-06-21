package ru.travelagency.controller.crud;

import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import ru.travelagency.dto.HotelDto;
import ru.travelagency.service.crud.HotelService;

import static ru.travelagency.security.PermissionCodes.HOTEL_READ;
import static ru.travelagency.security.PermissionCodes.HOTEL_WRITE;

@RestController
@RequestMapping("/api/hotels")
public class HotelController extends AbstractCrudController<HotelDto, Long, HotelService> {

    public HotelController(HotelService service) { super(service); }

    @Override
    @GetMapping
    @PreAuthorize("hasAuthority('" + HOTEL_READ + "')")
    public Page<HotelDto> list(Pageable pageable) { return super.list(pageable); }

    @Override
    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('" + HOTEL_READ + "')")
    public HotelDto getOne(@PathVariable Long id) { return super.getOne(id); }

    @Override
    @PostMapping
    @PreAuthorize("hasAuthority('" + HOTEL_WRITE + "')")
    @ResponseStatus(HttpStatus.CREATED)
    public HotelDto create(@RequestBody @Valid HotelDto dto) { return super.create(dto); }

    @Override
    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('" + HOTEL_WRITE + "')")
    public HotelDto update(@PathVariable Long id, @RequestBody @Valid HotelDto dto) { return super.update(id, dto); }

    @Override
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('" + HOTEL_WRITE + "')")
    public ResponseEntity<Void> delete(@PathVariable Long id) { return super.delete(id); }
}
