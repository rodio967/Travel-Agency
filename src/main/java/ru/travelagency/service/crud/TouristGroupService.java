package ru.travelagency.service.crud;

import org.springframework.stereotype.Service;
import ru.travelagency.domain.Flight;
import ru.travelagency.domain.TouristGroup;
import ru.travelagency.dto.core.TouristGroupDto;
import ru.travelagency.exception.NotFoundException;
import ru.travelagency.mapper.TouristGroupMapper;
import ru.travelagency.repository.FlightRepository;
import ru.travelagency.repository.TouristGroupRepository;

@Service
public class TouristGroupService extends AbstractCrudService<TouristGroup, Long, TouristGroupDto> {

    private final FlightRepository flightRepository;

    public TouristGroupService(TouristGroupRepository repository,
                               TouristGroupMapper mapper,
                               FlightRepository flightRepository) {
        super(repository, mapper, TouristGroup.class);
        this.flightRepository = flightRepository;
    }

    @Override
    protected void beforeCreate(TouristGroup entity, TouristGroupDto dto) {
        if (dto.getArrivalFlightId() != null) {
            entity.setArrivalFlight(loadFlight(dto.getArrivalFlightId()));
        }
        if (dto.getDepartureFlightId() != null) {
            entity.setDepartureFlight(loadFlight(dto.getDepartureFlightId()));
        }
    }

    @Override
    protected void beforeUpdate(TouristGroup entity, TouristGroupDto dto) {
        if (dto.getArrivalFlightId() != null) {
            entity.setArrivalFlight(loadFlight(dto.getArrivalFlightId()));
        }
        if (dto.getDepartureFlightId() != null) {
            entity.setDepartureFlight(loadFlight(dto.getDepartureFlightId()));
        }
    }

    private Flight loadFlight(Long id) {
        return flightRepository.findById(id)
                .orElseThrow(() -> NotFoundException.of(Flight.class, id));
    }
}
