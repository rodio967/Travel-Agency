package ru.travelagency.dto;


public record PermissionDto(
        Long id,
        String code,
        String category,
        String description
) {}
