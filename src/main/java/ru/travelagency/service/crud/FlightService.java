package ru.travelagency.service.crud;

import org.springframework.stereotype.Service;
import ru.travelagency.domain.Flight;
import ru.travelagency.dto.core.FlightDto;
import ru.travelagency.mapper.FlightMapper;
import ru.travelagency.repository.FlightRepository;

@Service
public class FlightService extends AbstractCrudService<Flight, Long, FlightDto> {

    public FlightService(FlightRepository repository, FlightMapper mapper) {
        super(repository, mapper, Flight.class);
    }
}
