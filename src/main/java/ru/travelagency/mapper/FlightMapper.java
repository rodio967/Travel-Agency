package ru.travelagency.mapper;

import org.springframework.stereotype.Component;
import ru.travelagency.domain.Flight;
import ru.travelagency.dto.core.FlightDto;

@Component
public class FlightMapper implements Mapper<Flight, FlightDto> {

    @Override
    public FlightDto toDto(Flight e) {
        if (e == null) return null;
        return FlightDto.builder()
                .id(e.getId())
                .flightNumber(e.getFlightNumber())
                .flightDate(e.getFlightDate())
                .flightType(e.getFlightType())
                .maxSeats(e.getMaxSeats())
                .maxCargoKg(e.getMaxCargoKg())
                .airportFees(e.getAirportFees())
                .build();
    }

    @Override
    public Flight toEntity(FlightDto dto) {
        if (dto == null) return null;
        Flight f = new Flight();
        f.setFlightNumber(dto.getFlightNumber());
        f.setFlightDate(dto.getFlightDate());
        f.setFlightType(dto.getFlightType());
        f.setMaxSeats(dto.getMaxSeats());
        f.setMaxCargoKg(dto.getMaxCargoKg());
        f.setAirportFees(dto.getAirportFees());
        return f;
    }

    @Override
    public void updateEntity(Flight e, FlightDto dto) {
        if (dto.getFlightNumber() != null) e.setFlightNumber(dto.getFlightNumber());
        if (dto.getFlightDate() != null) e.setFlightDate(dto.getFlightDate());
        if (dto.getFlightType() != null) e.setFlightType(dto.getFlightType());
        if (dto.getMaxSeats() != null) e.setMaxSeats(dto.getMaxSeats());
        if (dto.getMaxCargoKg() != null) e.setMaxCargoKg(dto.getMaxCargoKg());
        if (dto.getAirportFees() != null) e.setAirportFees(dto.getAirportFees());
    }
}
