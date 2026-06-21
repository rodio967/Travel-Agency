package ru.travelagency.dto.report;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;


public record VisitStatsDto(
        String passportNumber,
        String firstName,
        String lastName,
        String middleName,
        LocalDate birthDate,
        long visitCount,
        List<VisitDetail> visits
) {

    public record VisitDetail(
            Long touristId,
            Long groupId,
            String groupName,
            LocalDate arrivalDate,
            LocalDate departureDate,
            String category,
            List<HotelStay> hotels,
            List<ExcursionVisit> excursions,
            List<CargoItem> cargo
    ) {}


    public record HotelStay(
            Long hotelId,
            String hotelName,
            String roomNumber,
            LocalDate checkInDate,
            LocalDate checkOutDate
    ) {}


    public record ExcursionVisit(
            Long excursionId,
            String excursionName,
            LocalDate excursionDate,
            Long agencyId,
            String agencyName
    ) {}


    public record CargoItem(
            Long cargoId,
            String marking,
            String cargoType,
            Integer placesCount,
            BigDecimal weightKg,
            BigDecimal totalCost
    ) {}
}