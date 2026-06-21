package ru.travelagency.dto.report;

import java.math.BigDecimal;


public record CargoTurnoverDto(
        long totalPlaces,
        BigDecimal totalWeightKg,
        long flightsCount,
        long cargoFlightsCount,
        long mixedFlightsCount,
        long passengerFlightsCount
) {}
