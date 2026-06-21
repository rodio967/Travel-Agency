package ru.travelagency.service.crud;

import org.springframework.stereotype.Service;
import ru.travelagency.domain.Hotel;
import ru.travelagency.domain.HotelBooking;
import ru.travelagency.domain.Tourist;
import ru.travelagency.dto.HotelBookingDto;
import ru.travelagency.exception.NotFoundException;
import ru.travelagency.mapper.HotelBookingMapper;
import ru.travelagency.repository.HotelBookingRepository;
import ru.travelagency.repository.HotelRepository;
import ru.travelagency.repository.TouristRepository;

@Service
public class HotelBookingService extends AbstractCrudService<HotelBooking, Long, HotelBookingDto> {

    private final TouristRepository touristRepository;
    private final HotelRepository hotelRepository;

    public HotelBookingService(HotelBookingRepository repository,
                               HotelBookingMapper mapper,
                               TouristRepository touristRepository,
                               HotelRepository hotelRepository) {
        super(repository, mapper, HotelBooking.class);
        this.touristRepository = touristRepository;
        this.hotelRepository = hotelRepository;
    }

    @Override
    protected void beforeCreate(HotelBooking entity, HotelBookingDto dto) {
        entity.setTourist(loadTourist(dto.getTouristId()));
        entity.setHotel(loadHotel(dto.getHotelId()));
    }

    @Override
    protected void beforeUpdate(HotelBooking entity, HotelBookingDto dto) {
        if (dto.getTouristId() != null) entity.setTourist(loadTourist(dto.getTouristId()));
        if (dto.getHotelId() != null) entity.setHotel(loadHotel(dto.getHotelId()));
    }

    private Tourist loadTourist(Long id) {
        return touristRepository.findById(id)
                .orElseThrow(() -> NotFoundException.of(Tourist.class, id));
    }

    private Hotel loadHotel(Long id) {
        return hotelRepository.findById(id)
                .orElseThrow(() -> NotFoundException.of(Hotel.class, id));
    }
}
