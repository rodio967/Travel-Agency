package ru.travelagency.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import ru.travelagency.enums.Gender;
import ru.travelagency.enums.TouristType;

import java.time.LocalDate;
import java.util.Set;

public record ChildDto(
        Long id,
        @NotBlank String firstName,
        @NotBlank String lastName,
        String middleName,
        @NotNull Gender gender,
        @NotNull LocalDate birthDate,
        String passportNumber,
        @NotNull Long groupId,
        Set<Long> preferredHotelIds,
        @NotNull Long parentId
) implements TouristDto {

    @Override
    @JsonProperty("touristType")
    public TouristType touristType() {
        return TouristType.CHILD;
    }
}
