package ru.travelagency.controller.crud;

import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import ru.travelagency.dto.TouristDto;
import ru.travelagency.enums.TouristType;
import ru.travelagency.service.crud.TouristService;

import java.util.List;

import static ru.travelagency.security.PermissionCodes.TOURIST_READ;
import static ru.travelagency.security.PermissionCodes.TOURIST_WRITE;

@RestController
@RequestMapping("/api/tourists")
public class TouristController {

    private final TouristService service;

    public TouristController(TouristService service) {
        this.service = service;
    }

    @GetMapping
    @PreAuthorize("hasAuthority('" + TOURIST_READ + "')")
    public Page<TouristDto> list(Pageable pageable, @RequestParam(required = false) TouristType type) {
        if (type == null) {
            return service.findAll(pageable);
        }
        return service.findAllByType(pageable, type);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAuthority('" + TOURIST_READ + "')")
    public TouristDto getOne(@PathVariable Long id) {
        return service.findById(id);
    }

    @GetMapping("/by-passport/{passportNumber}")
    @PreAuthorize("hasAuthority('" + TOURIST_READ + "')")
    public List<TouristDto> findByPassport(@PathVariable String passportNumber) {
        return service.findVisitsByPassport(passportNumber);
    }

    @PostMapping
    @PreAuthorize("hasAuthority('" + TOURIST_WRITE + "')")
    @ResponseStatus(HttpStatus.CREATED)
    public TouristDto create(@RequestBody @Valid TouristDto dto) {
        return service.create(dto);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('" + TOURIST_WRITE + "')")
    public TouristDto update(@PathVariable Long id, @RequestBody @Valid TouristDto dto) {
        return service.update(id, dto);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('" + TOURIST_WRITE + "')")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
