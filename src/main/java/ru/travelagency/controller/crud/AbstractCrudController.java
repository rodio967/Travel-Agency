package ru.travelagency.controller.crud;

import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.travelagency.service.crud.AbstractCrudService;

public abstract class AbstractCrudController<DTO, ID, S extends AbstractCrudService<?, ID, DTO>> {

    protected final S service;

    protected AbstractCrudController(S service) {
        this.service = service;
    }

    @GetMapping
    public Page<DTO> list(Pageable pageable) {
        return service.findAll(pageable);
    }

    @GetMapping("/{id}")
    public DTO getOne(@PathVariable ID id) {
        return service.findById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public DTO create(@RequestBody @Valid DTO dto) {
        return service.create(dto);
    }

    @PutMapping("/{id}")
    public DTO update(@PathVariable ID id, @RequestBody @Valid DTO dto) {
        return service.update(id, dto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable ID id) {
        service.delete(id);
        return ResponseEntity.noContent().build();
    }
}
