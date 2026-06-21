package ru.travelagency.dto.core;

import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ExcursionAgencyDto {

    private Long id;

    @NotBlank
    private String name;

    private String contact;

    @DecimalMin("0.0") @DecimalMax("10.0")
    private BigDecimal qualityRating;
}
