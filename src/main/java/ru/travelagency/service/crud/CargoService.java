package ru.travelagency.service.crud;

import org.springframework.stereotype.Service;
import ru.travelagency.domain.Cargo;
import ru.travelagency.domain.Flight;
import ru.travelagency.domain.Tourist;
import ru.travelagency.dto.CargoDto;
import ru.travelagency.exception.NotFoundException;
import ru.travelagency.mapper.CargoMapper;
import ru.travelagency.repository.CargoRepository;
import ru.travelagency.repository.FlightRepository;
import ru.travelagency.repository.TouristRepository;


@Service
public class CargoService extends AbstractCrudService<Cargo, Long, CargoDto> {

    private final TouristRepository touristRepository;
    private final FlightRepository flightRepository;

    public CargoService(CargoRepository repository,
                        CargoMapper mapper,
                        TouristRepository touristRepository,
                        FlightRepository flightRepository) {
        super(repository, mapper, Cargo.class);
        this.touristRepository = touristRepository;
        this.flightRepository = flightRepository;
    }

    @Override
    protected void beforeCreate(Cargo entity, CargoDto dto) {
        entity.setTourist(loadTourist(dto.getTouristId()));
        entity.setFlight(loadFlight(dto.getFlightId()));
    }

    @Override
    protected void beforeUpdate(Cargo entity, CargoDto dto) {
        if (dto.getTouristId() != null) entity.setTourist(loadTourist(dto.getTouristId()));
        if (dto.getFlightId() != null) entity.setFlight(loadFlight(dto.getFlightId()));
    }

    private Tourist loadTourist(Long id) {
        return touristRepository.findById(id)
                .orElseThrow(() -> NotFoundException.of(Tourist.class, id));
    }

    private Flight loadFlight(Long id) {
        return flightRepository.findById(id)
                .orElseThrow(() -> NotFoundException.of(Flight.class, id));
    }
}
