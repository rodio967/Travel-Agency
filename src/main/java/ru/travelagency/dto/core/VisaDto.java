package ru.travelagency.dto.core;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class VisaDto {

    private Long id;

    @NotNull
    private Long touristId;

    @NotBlank
    private String visaNumber;

    @NotNull
    private LocalDate issueDate;

    @NotNull
    private LocalDate expiryDate;

    private BigDecimal cost;
}
