package ru.travelagency.mapper;

import org.springframework.stereotype.Component;
import ru.travelagency.domain.FinancialEntry;
import ru.travelagency.dto.core.FinancialEntryDto;

@Component
public class FinancialEntryMapper implements Mapper<FinancialEntry, FinancialEntryDto> {

    @Override
    public FinancialEntryDto toDto(FinancialEntry e) {
        if (e == null) return null;
        return FinancialEntryDto.builder()
                .id(e.getId())
                .groupId(e.getGroup() != null ? e.getGroup().getId() : null)
                .touristId(e.getTourist() != null ? e.getTourist().getId() : null)
                .entryType(e.getEntryType())
                .category(e.getCategory())
                .amount(e.getAmount())
                .entryDate(e.getEntryDate())
                .description(e.getDescription())
                .build();
    }

    @Override
    public FinancialEntry toEntity(FinancialEntryDto dto) {
        if (dto == null) return null;
        FinancialEntry e = new FinancialEntry();
        e.setEntryType(dto.getEntryType());
        e.setCategory(dto.getCategory());
        e.setAmount(dto.getAmount());
        e.setEntryDate(dto.getEntryDate());
        e.setDescription(dto.getDescription());
        return e;
    }

    @Override
    public void updateEntity(FinancialEntry e, FinancialEntryDto dto) {
        if (dto.getEntryType() != null) e.setEntryType(dto.getEntryType());
        if (dto.getCategory() != null) e.setCategory(dto.getCategory());
        if (dto.getAmount() != null) e.setAmount(dto.getAmount());
        if (dto.getEntryDate() != null) e.setEntryDate(dto.getEntryDate());
        if (dto.getDescription() != null) e.setDescription(dto.getDescription());
    }
}