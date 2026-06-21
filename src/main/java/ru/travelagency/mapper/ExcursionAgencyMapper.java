package ru.travelagency.mapper;

import org.springframework.stereotype.Component;
import ru.travelagency.domain.ExcursionAgency;
import ru.travelagency.dto.ExcursionAgencyDto;

@Component
public class ExcursionAgencyMapper implements Mapper<ExcursionAgency, ExcursionAgencyDto> {

    @Override
    public ExcursionAgencyDto toDto(ExcursionAgency e) {
        if (e == null) return null;
        return ExcursionAgencyDto.builder()
                .id(e.getId())
                .name(e.getName())
                .contact(e.getContact())
                .qualityRating(e.getQualityRating())
                .build();
    }

    @Override
    public ExcursionAgency toEntity(ExcursionAgencyDto dto) {
        if (dto == null) return null;
        ExcursionAgency a = new ExcursionAgency();
        a.setName(dto.getName());
        a.setContact(dto.getContact());
        a.setQualityRating(dto.getQualityRating());
        return a;
    }

    @Override
    public void updateEntity(ExcursionAgency e, ExcursionAgencyDto dto) {
        if (dto.getName() != null) e.setName(dto.getName());
        if (dto.getContact() != null) e.setContact(dto.getContact());
        if (dto.getQualityRating() != null) e.setQualityRating(dto.getQualityRating());
    }
}
