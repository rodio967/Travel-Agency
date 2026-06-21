package ru.travelagency.mapper;


public interface Mapper<E, DTO> {

    DTO toDto(E entity);

    E toEntity(DTO dto);

    void updateEntity(E entity, DTO dto);
}
