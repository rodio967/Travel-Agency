package ru.travelagency.mapper;

import org.springframework.stereotype.Component;
import ru.travelagency.domain.HotelBooking;
import ru.travelagency.dto.core.HotelBookingDto;

@Component
public class HotelBookingMapper implements Mapper<HotelBooking, HotelBookingDto> {

    @Override
    public HotelBookingDto toDto(HotelBooking b) {
        if (b == null) return null;
        return HotelBookingDto.builder()
                .id(b.getId())
                .touristId(b.getTourist() != null ? b.getTourist().getId() : null)
                .hotelId(b.getHotel() != null ? b.getHotel().getId() : null)
                .roomNumber(b.getRoomNumber())
                .checkInDate(b.getCheckInDate())
                .checkOutDate(b.getCheckOutDate())
                .cost(b.getCost())
                .build();
    }

    @Override
    public HotelBooking toEntity(HotelBookingDto dto) {
        if (dto == null) return null;
        HotelBooking b = new HotelBooking();
        b.setRoomNumber(dto.getRoomNumber());
        b.setCheckInDate(dto.getCheckInDate());
        b.setCheckOutDate(dto.getCheckOutDate());
        b.setCost(dto.getCost());
        return b;
    }

    @Override
    public void updateEntity(HotelBooking b, HotelBookingDto dto) {
        if (dto.getRoomNumber() != null) b.setRoomNumber(dto.getRoomNumber());
        if (dto.getCheckInDate() != null) b.setCheckInDate(dto.getCheckInDate());
        if (dto.getCheckOutDate() != null) b.setCheckOutDate(dto.getCheckOutDate());
        if (dto.getCost() != null) b.setCost(dto.getCost());
    }
}
