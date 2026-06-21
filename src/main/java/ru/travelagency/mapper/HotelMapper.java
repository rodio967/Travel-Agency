package ru.travelagency.mapper;

import org.springframework.stereotype.Component;
import ru.travelagency.domain.Hotel;
import ru.travelagency.dto.HotelDto;

@Component
public class HotelMapper implements Mapper<Hotel, HotelDto> {

    @Override
    public HotelDto toDto(Hotel entity) {
        if (entity == null) return null;
        return HotelDto.builder()
                .id(entity.getId())
                .name(entity.getName())
                .address(entity.getAddress())
                .stars(entity.getStars())
                .pricePerNight(entity.getPricePerNight())
                .build();
    }

    @Override
    public Hotel toEntity(HotelDto dto) {
        if (dto == null) return null;
        Hotel hotel = new Hotel();
        hotel.setName(dto.getName());
        hotel.setAddress(dto.getAddress());
        hotel.setStars(dto.getStars());
        hotel.setPricePerNight(dto.getPricePerNight());
        return hotel;
    }

    @Override
    public void updateEntity(Hotel entity, HotelDto dto) {
        if (dto.getName() != null) entity.setName(dto.getName());
        if (dto.getAddress() != null) entity.setAddress(dto.getAddress());
        if (dto.getStars() != null) entity.setStars(dto.getStars());
        if (dto.getPricePerNight() != null) entity.setPricePerNight(dto.getPricePerNight());
    }
}
