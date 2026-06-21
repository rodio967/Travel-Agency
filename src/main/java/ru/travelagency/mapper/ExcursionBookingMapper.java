package ru.travelagency.mapper;

import org.springframework.stereotype.Component;
import ru.travelagency.domain.ExcursionBooking;
import ru.travelagency.dto.core.ExcursionBookingDto;

@Component
public class ExcursionBookingMapper implements Mapper<ExcursionBooking, ExcursionBookingDto> {

    @Override
    public ExcursionBookingDto toDto(ExcursionBooking b) {
        if (b == null) return null;
        return ExcursionBookingDto.builder()
                .id(b.getId())
                .touristId(b.getTourist() != null ? b.getTourist().getId() : null)
                .excursionId(b.getExcursion() != null ? b.getExcursion().getId() : null)
                .bookedAt(b.getBookedAt())
                .build();
    }

    @Override
    public ExcursionBooking toEntity(ExcursionBookingDto dto) {
        if (dto == null) return null;
        ExcursionBooking b = new ExcursionBooking();
        b.setBookedAt(dto.getBookedAt());
        return b;
    }

    @Override
    public void updateEntity(ExcursionBooking b, ExcursionBookingDto dto) {
        if (dto.getBookedAt() != null) b.setBookedAt(dto.getBookedAt());
    }
}
