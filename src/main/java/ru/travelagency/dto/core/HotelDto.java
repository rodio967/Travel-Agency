package ru.travelagency.dto.core;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.math.BigDecimal;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HotelDto {

    private Long id;

    @NotBlank
    private String name;

    @NotBlank
    private String address;

    @Min(1) @Max(5)
    private Integer stars;

    private BigDecimal pricePerNight;
}
