package ru.travelagency.dto.report;


public record ExcursionPopularityDto(
        Long excursionId,
        String excursionName,
        Long agencyId,
        String agencyName,
        long bookingsCount
) {}
