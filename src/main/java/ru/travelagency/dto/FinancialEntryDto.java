package ru.travelagency.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.travelagency.enums.EntryType;
import ru.travelagency.enums.FinancialCategory;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FinancialEntryDto {

    private Long id;

    @NotNull
    private Long groupId;

    private Long touristId;

    @NotNull
    private EntryType entryType;

    @NotNull
    private FinancialCategory category;

    @NotNull @PositiveOrZero
    private BigDecimal amount;

    @NotNull
    private LocalDate entryDate;

    private String description;
}