package ru.travelagency.service.crud;

import org.springframework.stereotype.Service;
import ru.travelagency.domain.Excursion;
import ru.travelagency.domain.ExcursionAgency;
import ru.travelagency.dto.core.ExcursionDto;
import ru.travelagency.exception.NotFoundException;
import ru.travelagency.mapper.ExcursionMapper;
import ru.travelagency.repository.ExcursionAgencyRepository;
import ru.travelagency.repository.ExcursionRepository;

@Service
public class ExcursionService extends AbstractCrudService<Excursion, Long, ExcursionDto> {

    private final ExcursionAgencyRepository agencyRepository;

    public ExcursionService(ExcursionRepository repository,
                            ExcursionMapper mapper,
                            ExcursionAgencyRepository agencyRepository) {
        super(repository, mapper, Excursion.class);
        this.agencyRepository = agencyRepository;
    }

    @Override
    protected void beforeCreate(Excursion entity, ExcursionDto dto) {
        entity.setAgency(loadAgency(dto.getAgencyId()));
    }


    @Override
    protected void beforeUpdate(Excursion entity, ExcursionDto dto) {
        if (dto.getAgencyId() != null) {
            entity.setAgency(loadAgency(dto.getAgencyId()));
        }
    }

    private ExcursionAgency loadAgency(Long id) {
        return agencyRepository.findById(id)
                .orElseThrow(() -> NotFoundException.of(ExcursionAgency.class, id));
    }
}
