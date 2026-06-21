package ru.travelagency.dto.report;

import java.math.BigDecimal;


public record CargoTypeStatsDto(
        String cargoType,
        long entries,
        BigDecimal totalWeightKg,
        BigDecimal sharePercent
) {}
