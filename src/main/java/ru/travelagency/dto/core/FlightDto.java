package ru.travelagency.dto.core;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.travelagency.enums.FlightType;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FlightDto {

    private Long id;

    @NotBlank
    private String flightNumber;

    @NotNull
    private LocalDate flightDate;

    @NotNull
    private FlightType flightType;

    @Positive
    private Integer maxSeats;

    private BigDecimal maxCargoKg;

    private BigDecimal airportFees;
}
