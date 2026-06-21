package ru.travelagency.mapper;

import org.springframework.stereotype.Component;
import ru.travelagency.domain.Visa;
import ru.travelagency.dto.core.VisaDto;

@Component
public class VisaMapper implements Mapper<Visa, VisaDto> {

    @Override
    public VisaDto toDto(Visa v) {
        if (v == null) return null;
        return VisaDto.builder()
                .id(v.getId())
                .touristId(v.getTourist() != null ? v.getTourist().getId() : null)
                .visaNumber(v.getVisaNumber())
                .issueDate(v.getIssueDate())
                .expiryDate(v.getExpiryDate())
                .cost(v.getCost())
                .build();
    }

    @Override
    public Visa toEntity(VisaDto dto) {
        if (dto == null) return null;
        Visa v = new Visa();
        v.setVisaNumber(dto.getVisaNumber());
        v.setIssueDate(dto.getIssueDate());
        v.setExpiryDate(dto.getExpiryDate());
        v.setCost(dto.getCost());
        return v;
    }

    @Override
    public void updateEntity(Visa v, VisaDto dto) {
        if (dto.getVisaNumber() != null) v.setVisaNumber(dto.getVisaNumber());
        if (dto.getIssueDate() != null) v.setIssueDate(dto.getIssueDate());
        if (dto.getExpiryDate() != null) v.setExpiryDate(dto.getExpiryDate());
        if (dto.getCost() != null) v.setCost(dto.getCost());
    }
}
