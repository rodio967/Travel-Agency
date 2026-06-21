package ru.travelagency.service.crud;

import org.springframework.stereotype.Service;
import ru.travelagency.domain.ExcursionAgency;
import ru.travelagency.dto.ExcursionAgencyDto;
import ru.travelagency.mapper.ExcursionAgencyMapper;
import ru.travelagency.repository.ExcursionAgencyRepository;

@Service
public class ExcursionAgencyService extends AbstractCrudService<ExcursionAgency, Long, ExcursionAgencyDto> {

    public ExcursionAgencyService(ExcursionAgencyRepository repository,
                                  ExcursionAgencyMapper mapper) {
        super(repository, mapper, ExcursionAgency.class);
    }
}
