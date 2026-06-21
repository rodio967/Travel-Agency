package ru.travelagency.dto.report;

import java.math.BigDecimal;
import java.util.List;


public record FlightTouristInfoDto(
        Long touristId,
        String firstName,
        String lastName,
        String passportNumber,
        String category,
        Long groupId,
        String groupName,
        List<String> hotels,
        List<CargoSummary> cargoList
) {
    public record CargoSummary(
            String marking,
            String cargoType,
            Integer placesCount,
            BigDecimal weightKg,
            BigDecimal totalCost
    ) {}
}
