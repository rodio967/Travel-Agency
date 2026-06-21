package ru.travelagency.mapper;

import org.springframework.stereotype.Component;
import ru.travelagency.domain.TouristGroup;
import ru.travelagency.dto.core.TouristGroupDto;

@Component
public class TouristGroupMapper implements Mapper<TouristGroup, TouristGroupDto> {

    @Override
    public TouristGroupDto toDto(TouristGroup g) {
        if (g == null) return null;
        return TouristGroupDto.builder()
                .id(g.getId())
                .name(g.getName())
                .arrivalDate(g.getArrivalDate())
                .departureDate(g.getDepartureDate())
                .arrivalFlightId(g.getArrivalFlight() != null ? g.getArrivalFlight().getId() : null)
                .departureFlightId(g.getDepartureFlight() != null ? g.getDepartureFlight().getId() : null)
                .build();
    }

    @Override
    public TouristGroup toEntity(TouristGroupDto dto) {
        if (dto == null) return null;
        TouristGroup g = new TouristGroup();
        g.setName(dto.getName());
        g.setArrivalDate(dto.getArrivalDate());
        g.setDepartureDate(dto.getDepartureDate());
        return g;
    }

    @Override
    public void updateEntity(TouristGroup g, TouristGroupDto dto) {
        if (dto.getName() != null) g.setName(dto.getName());
        if (dto.getArrivalDate() != null) g.setArrivalDate(dto.getArrivalDate());
        if (dto.getDepartureDate() != null) g.setDepartureDate(dto.getDepartureDate());
    }
}
