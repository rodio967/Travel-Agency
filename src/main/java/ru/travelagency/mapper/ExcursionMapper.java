package ru.travelagency.mapper;

import org.springframework.stereotype.Component;
import ru.travelagency.domain.Excursion;
import ru.travelagency.dto.ExcursionDto;

@Component
public class ExcursionMapper implements Mapper<Excursion, ExcursionDto> {

    @Override
    public ExcursionDto toDto(Excursion e) {
        if (e == null) return null;
        return ExcursionDto.builder()
                .id(e.getId())
                .agencyId(e.getAgency() != null ? e.getAgency().getId() : null)
                .name(e.getName())
                .description(e.getDescription())
                .price(e.getPrice())
                .excursionDate(e.getExcursionDate())
                .build();
    }

    @Override
    public Excursion toEntity(ExcursionDto dto) {
        if (dto == null) return null;
        Excursion e = new Excursion();
        e.setName(dto.getName());
        e.setDescription(dto.getDescription());
        e.setPrice(dto.getPrice());
        e.setExcursionDate(dto.getExcursionDate());
        return e;
    }

    @Override
    public void updateEntity(Excursion e, ExcursionDto dto) {
        if (dto.getName() != null) e.setName(dto.getName());
        if (dto.getDescription() != null) e.setDescription(dto.getDescription());
        if (dto.getPrice() != null) e.setPrice(dto.getPrice());
        if (dto.getExcursionDate() != null) e.setExcursionDate(dto.getExcursionDate());
    }
}
