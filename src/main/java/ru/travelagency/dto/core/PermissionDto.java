package ru.travelagency.dto.core;


public record PermissionDto(
        Long id,
        String code,
        String category,
        String description
) {}
