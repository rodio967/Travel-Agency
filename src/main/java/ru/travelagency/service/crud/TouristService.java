package ru.travelagency.service.crud;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.travelagency.domain.*;
import ru.travelagency.dto.core.ChildDto;
import ru.travelagency.dto.core.TouristDto;
import ru.travelagency.enums.TouristType;
import ru.travelagency.exception.BusinessRuleException;
import ru.travelagency.exception.NotFoundException;
import ru.travelagency.mapper.TouristMapper;
import ru.travelagency.repository.HotelRepository;
import ru.travelagency.repository.TouristGroupRepository;
import ru.travelagency.repository.TouristRepository;

import java.util.HashSet;
import java.util.List;
import java.util.Set;


@Service
public class TouristService {

    private final TouristRepository touristRepository;
    private final TouristGroupRepository groupRepository;
    private final HotelRepository hotelRepository;
    private final TouristMapper mapper;

    public TouristService(TouristRepository touristRepository,
                          TouristGroupRepository groupRepository,
                          HotelRepository hotelRepository,
                          TouristMapper mapper) {
        this.touristRepository = touristRepository;
        this.groupRepository = groupRepository;
        this.hotelRepository = hotelRepository;
        this.mapper = mapper;
    }

    @Transactional(readOnly = true)
    public TouristDto findById(Long id) {
        Tourist t = touristRepository.findById(id)
                .orElseThrow(() -> NotFoundException.of(Tourist.class, id));
        return mapper.toDto(t);
    }

    @Transactional(readOnly = true)
    public Page<TouristDto> findAll(Pageable pageable) {
        return touristRepository.findAll(pageable).map(mapper::toDto);
    }

    @Transactional(readOnly = true)
    public Page<TouristDto> findAllByType(Pageable pageable, TouristType type) {
        Class<? extends Tourist> entityClass = switch(type) {
            case REST -> RestTourist.class;
            case SHOP -> ShopTourist.class;
            case CHILD -> Child.class;
        };

        return touristRepository.findAllByType(entityClass, pageable)
                .map(mapper::toDto);
    }

    @Transactional(readOnly = true)
    public List<TouristDto> findVisitsByPassport(String passportNumber) {
        return touristRepository.findAllByPassportNumber(passportNumber).stream()
                .map(mapper::toDto)
                .toList();
    }

    @Transactional
    public TouristDto create(TouristDto dto) {
        Tourist entity = mapper.toEntity(dto);

        TouristGroup group = groupRepository.findById(dto.groupId())
                .orElseThrow(() -> NotFoundException.of(TouristGroup.class, dto.groupId()));
        entity.setGroup(group);

        entity.setPreferredHotels(loadHotels(dto.preferredHotelIds()));

        if (dto instanceof ChildDto child) {
            Tourist parent = touristRepository.findById(child.parentId())
                    .orElseThrow(() -> NotFoundException.of(Tourist.class, child.parentId()));

            if (parent instanceof Child) {
                throw new BusinessRuleException("Parent of CHILD must not be a CHILD himself");
            }
            ((Child) entity).setParent(parent);
        }

        Tourist saved = touristRepository.save(entity);
        return mapper.toDto(saved);
    }

    @Transactional
    public TouristDto update(Long id, TouristDto dto) {
        Tourist entity = touristRepository.findById(id)
                .orElseThrow(() -> NotFoundException.of(Tourist.class, id));

        mapper.updateEntity(entity, dto);

        if (dto.groupId() != null && !dto.groupId().equals(
                entity.getGroup() != null ? entity.getGroup().getId() : null)) {
            TouristGroup group = groupRepository.findById(dto.groupId())
                    .orElseThrow(() -> NotFoundException.of(TouristGroup.class, dto.groupId()));
            entity.setGroup(group);
        }

        if (dto.preferredHotelIds() != null) {
            entity.setPreferredHotels(loadHotels(dto.preferredHotelIds()));
        }

        if (dto instanceof ChildDto child && child.parentId() != null
                && entity instanceof Child childEntity) {
            Tourist parent = touristRepository.findById(child.parentId())
                    .orElseThrow(() -> NotFoundException.of(Tourist.class, child.parentId()));
            if (parent instanceof Child) {
                throw new BusinessRuleException(
                        "Parent of CHILD must not be a CHILD himself");
            }
            childEntity.setParent(parent);
        }

        Tourist saved = touristRepository.save(entity);
        return mapper.toDto(saved);
    }

    @Transactional
    public void delete(Long id) {
        if (!touristRepository.existsById(id)) {
            throw NotFoundException.of(Tourist.class, id);
        }
        touristRepository.deleteById(id);
    }


    private Set<Hotel> loadHotels(Set<Long> ids) {
        if (ids == null || ids.isEmpty()) return new HashSet<>();
        Set<Hotel> hotels = new HashSet<>(hotelRepository.findAllById(ids));
        if (hotels.size() != ids.size()) {
            ids.stream()
                    .filter(id -> hotels.stream().noneMatch(h -> h.getId().equals(id)))
                    .findFirst()
                    .ifPresent(missing -> {
                        throw NotFoundException.of(Hotel.class, missing);
                    });
        }
        return hotels;
    }
}
