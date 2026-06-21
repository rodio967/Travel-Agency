package ru.travelagency.service.crud;

import org.springframework.stereotype.Service;
import ru.travelagency.domain.Hotel;
import ru.travelagency.dto.core.HotelDto;
import ru.travelagency.mapper.HotelMapper;
import ru.travelagency.repository.HotelRepository;

@Service
public class HotelService extends AbstractCrudService<Hotel, Long, HotelDto> {

    public HotelService(HotelRepository repository, HotelMapper mapper) {
        super(repository, mapper, Hotel.class);
    }
}
