package ru.travelagency.service.crud;

import org.springframework.stereotype.Service;
import ru.travelagency.domain.Tourist;
import ru.travelagency.domain.Visa;
import ru.travelagency.dto.core.VisaDto;
import ru.travelagency.exception.NotFoundException;
import ru.travelagency.mapper.VisaMapper;
import ru.travelagency.repository.TouristRepository;
import ru.travelagency.repository.VisaRepository;


@Service
public class VisaService extends AbstractCrudService<Visa, Long, VisaDto> {

    private final TouristRepository touristRepository;

    public VisaService(VisaRepository repository,
                       VisaMapper mapper,
                       TouristRepository touristRepository) {
        super(repository, mapper, Visa.class);
        this.touristRepository = touristRepository;
    }

    @Override
    protected void beforeCreate(Visa entity, VisaDto dto) {
        entity.setTourist(loadTourist(dto.getTouristId()));
    }

    @Override
    protected void beforeUpdate(Visa entity, VisaDto dto) {
        if (dto.getTouristId() != null) entity.setTourist(loadTourist(dto.getTouristId()));
    }

    private Tourist loadTourist(Long id) {
        return touristRepository.findById(id)
                .orElseThrow(() -> NotFoundException.of(Tourist.class, id));
    }
}
