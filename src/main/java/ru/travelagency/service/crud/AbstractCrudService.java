package ru.travelagency.service.crud;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;
import ru.travelagency.exception.NotFoundException;
import ru.travelagency.mapper.Mapper;


public abstract class AbstractCrudService<E, ID, DTO> {

    protected final JpaRepository<E, ID> repository;
    protected final Mapper<E, DTO> mapper;
    protected final Class<E> entityClass;

    protected AbstractCrudService(JpaRepository<E, ID> repository,
                                  Mapper<E, DTO> mapper,
                                  Class<E> entityClass) {
        this.repository = repository;
        this.mapper = mapper;
        this.entityClass = entityClass;
    }


    @Transactional(readOnly = true)
    public DTO findById(ID id) {
        E entity = repository.findById(id)
                .orElseThrow(() -> NotFoundException.of(entityClass, id));
        return mapper.toDto(entity);
    }


    @Transactional(readOnly = true)
    public Page<DTO> findAll(Pageable pageable) {
        return repository.findAll(pageable).map(mapper::toDto);
    }


    @Transactional
    public DTO create(DTO dto) {
        E entity = mapper.toEntity(dto);
        beforeCreate(entity, dto);
        E saved = repository.save(entity);
        return mapper.toDto(saved);
    }


    @Transactional
    public DTO update(ID id, DTO dto) {
        E entity = repository.findById(id)
                .orElseThrow(() -> NotFoundException.of(entityClass, id));
        mapper.updateEntity(entity, dto);
        beforeUpdate(entity, dto);
        E saved = repository.save(entity);
        return mapper.toDto(saved);
    }


    @Transactional
    public void delete(ID id) {
        if (!repository.existsById(id)) {
            throw NotFoundException.of(entityClass, id);
        }
        repository.deleteById(id);
    }

    protected void beforeCreate(E entity, DTO dto) {}

    protected void beforeUpdate(E entity, DTO dto) {}
}
