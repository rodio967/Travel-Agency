package ru.travelagency.dto.core;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CargoDto {

    private Long id;

    @NotNull
    private Long touristId;

    @NotNull
    private Long flightId;

    private String marking;

    private String cargoType;

    @Positive
    private Integer placesCount;

    @PositiveOrZero
    private BigDecimal weightKg;

    @PositiveOrZero
    private BigDecimal volumetricWeight;

    @PositiveOrZero
    private BigDecimal packagingCost;

    @PositiveOrZero
    private BigDecimal insuranceCost;

    @PositiveOrZero
    private BigDecimal totalCost;
}
