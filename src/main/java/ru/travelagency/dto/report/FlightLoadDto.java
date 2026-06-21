package ru.travelagency.dto.report;

import java.math.BigDecimal;
import java.time.LocalDate;


public record FlightLoadDto(
        Long flightId,
        String flightNumber,
        LocalDate flightDate,
        long cargoEntries,
        long totalPlaces,
        BigDecimal totalWeightKg,
        BigDecimal totalVolumetricWeight
) {}
