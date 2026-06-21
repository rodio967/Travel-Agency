package ru.travelagency.dto.core;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import ru.travelagency.enums.Gender;
import ru.travelagency.enums.TouristType;

import java.time.LocalDate;
import java.util.Set;


@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.EXISTING_PROPERTY,
        property = "touristType",
        visible = true
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = RestTouristDto.class, name = "REST"),
        @JsonSubTypes.Type(value = ShopTouristDto.class, name = "SHOP"),
        @JsonSubTypes.Type(value = ChildDto.class, name = "CHILD")
})
public sealed interface TouristDto permits RestTouristDto, ShopTouristDto, ChildDto {

    TouristType touristType();

    Long id();
    String firstName();
    String lastName();
    String middleName();
    Gender gender();
    LocalDate birthDate();
    String passportNumber();
    Long groupId();
    Set<Long> preferredHotelIds();
}
