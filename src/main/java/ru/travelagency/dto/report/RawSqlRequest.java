package ru.travelagency.dto.report;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;

import java.util.List;
import java.util.Map;


public record RawSqlRequest(
        @NotBlank String sql,
        @Positive @Max(10000) Integer limit
) {}

