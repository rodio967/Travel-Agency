package ru.travelagency.service.crud;

import org.springframework.stereotype.Service;
import ru.travelagency.domain.Excursion;
import ru.travelagency.domain.ExcursionBooking;
import ru.travelagency.domain.Tourist;
import ru.travelagency.dto.core.ExcursionBookingDto;
import ru.travelagency.exception.NotFoundException;
import ru.travelagency.mapper.ExcursionBookingMapper;
import ru.travelagency.repository.ExcursionBookingRepository;
import ru.travelagency.repository.ExcursionRepository;
import ru.travelagency.repository.TouristRepository;

import java.time.LocalDateTime;


@Service
public class ExcursionBookingService extends AbstractCrudService<ExcursionBooking, Long, ExcursionBookingDto> {

    private final TouristRepository touristRepository;
    private final ExcursionRepository excursionRepository;

    public ExcursionBookingService(ExcursionBookingRepository repository,
                                   ExcursionBookingMapper mapper,
                                   TouristRepository touristRepository,
                                   ExcursionRepository excursionRepository) {
        super(repository, mapper, ExcursionBooking.class);
        this.touristRepository = touristRepository;
        this.excursionRepository = excursionRepository;
    }

    @Override
    protected void beforeCreate(ExcursionBooking entity, ExcursionBookingDto dto) {
        entity.setTourist(loadTourist(dto.getTouristId()));
        entity.setExcursion(loadExcursion(dto.getExcursionId()));
        if (entity.getBookedAt() == null) {
            entity.setBookedAt(LocalDateTime.now());
        }
    }

    @Override
    protected void beforeUpdate(ExcursionBooking entity, ExcursionBookingDto dto) {
        if (dto.getTouristId() != null) entity.setTourist(loadTourist(dto.getTouristId()));
        if (dto.getExcursionId() != null) entity.setExcursion(loadExcursion(dto.getExcursionId()));
    }

    private Tourist loadTourist(Long id) {
        return touristRepository.findById(id)
                .orElseThrow(() -> NotFoundException.of(Tourist.class, id));
    }

    private Excursion loadExcursion(Long id) {
        return excursionRepository.findById(id)
                .orElseThrow(() -> NotFoundException.of(Excursion.class, id));
    }
}
