package ru.travelagency.dto.core;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ExcursionBookingDto {

    private Long id;

    @NotNull
    private Long touristId;

    @NotNull
    private Long excursionId;

    private LocalDateTime bookedAt;
}
