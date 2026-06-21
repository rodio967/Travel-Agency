package ru.travelagency.dto.report;

import java.math.BigDecimal;


public record AgencyQualityDto(
        Long agencyId,
        String agencyName,
        BigDecimal qualityRating,
        long excursionsCount,
        long totalBookings
) {}
