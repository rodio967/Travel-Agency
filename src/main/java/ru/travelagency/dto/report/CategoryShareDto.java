package ru.travelagency.dto.report;

import java.math.BigDecimal;


public record CategoryShareDto(
        long restCount,
        long shopCount,
        long childCount,
        long total,
        BigDecimal restPercent,
        BigDecimal shopPercent,
        BigDecimal childPercent
) {}
