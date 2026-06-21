package ru.travelagency.dto.core;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import ru.travelagency.enums.Gender;
import ru.travelagency.enums.TouristType;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Set;

public record ShopTouristDto(
        Long id,
        @NotBlank String firstName,
        @NotBlank String lastName,
        String middleName,
        @NotNull Gender gender,
        @NotNull LocalDate birthDate,
        @NotBlank String passportNumber,
        @NotNull Long groupId,
        Set<Long> preferredHotelIds,
        @Positive BigDecimal cargoLimitKg,
        String preferredCargoType
) implements TouristDto {

    @Override
    @JsonProperty("touristType")
    public TouristType touristType() {
        return TouristType.SHOP;
    }
}
