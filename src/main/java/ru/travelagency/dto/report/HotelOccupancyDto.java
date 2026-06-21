package ru.travelagency.dto.report;


public record HotelOccupancyDto(
        Long hotelId,
        String hotelName,
        long roomsOccupied,
        long touristsCount
) {}
