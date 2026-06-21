package ru.travelagency.mapper;

import org.hibernate.Hibernate;
import org.springframework.stereotype.Component;
import ru.travelagency.domain.*;
import ru.travelagency.dto.*;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;


@Component
public class TouristMapper {

    public TouristDto toDto(Tourist t) {
        if (t == null) return null;
        Tourist unwrapped = (Tourist) Hibernate.unproxy(t);
        if (unwrapped instanceof RestTourist rt) return toDto(rt);
        if (unwrapped instanceof ShopTourist st) return toDto(st);
        if (unwrapped instanceof Child c) return toDto(c);
        throw new IllegalStateException("Unknown Tourist subtype: " + unwrapped.getClass());
    }

    public RestTouristDto toDto(RestTourist t) {
        return new RestTouristDto(
                t.getId(),
                t.getFirstName(),
                t.getLastName(),
                t.getMiddleName(),
                t.getGender(),
                t.getBirthDate(),
                t.getPassportNumber(),
                t.getGroup() != null ? t.getGroup().getId() : null,
                hotelIds(t),
                t.getPreferredExcursionType()
        );
    }

    public ShopTouristDto toDto(ShopTourist t) {
        return new ShopTouristDto(
                t.getId(),
                t.getFirstName(),
                t.getLastName(),
                t.getMiddleName(),
                t.getGender(),
                t.getBirthDate(),
                t.getPassportNumber(),
                t.getGroup() != null ? t.getGroup().getId() : null,
                hotelIds(t),
                t.getCargoLimitKg(),
                t.getPreferredCargoType()
        );
    }

    public ChildDto toDto(Child t) {
        return new ChildDto(
                t.getId(),
                t.getFirstName(),
                t.getLastName(),
                t.getMiddleName(),
                t.getGender(),
                t.getBirthDate(),
                t.getPassportNumber(),
                t.getGroup() != null ? t.getGroup().getId() : null,
                hotelIds(t),
                t.getParent() != null ? t.getParent().getId() : null
        );
    }


    public Tourist toEntity(TouristDto dto) {
        return switch (dto) {
            case RestTouristDto r -> {
                RestTourist e = new RestTourist();
                fillCommon(e, r.firstName(), r.lastName(), r.middleName(),
                        r.gender(), r.birthDate(), r.passportNumber());
                e.setPreferredExcursionType(r.preferredExcursionType());
                yield e;
            }
            case ShopTouristDto s -> {
                ShopTourist e = new ShopTourist();
                fillCommon(e, s.firstName(), s.lastName(), s.middleName(),
                        s.gender(), s.birthDate(), s.passportNumber());
                e.setCargoLimitKg(s.cargoLimitKg());
                e.setPreferredCargoType(s.preferredCargoType());
                yield e;
            }
            case ChildDto c -> {
                Child e = new Child();
                fillCommon(e, c.firstName(), c.lastName(), c.middleName(),
                        c.gender(), c.birthDate(), c.passportNumber());
                yield e;
            }
        };
    }


    public void updateEntity(Tourist entity, TouristDto dto) {
        if (entity instanceof RestTourist && !(dto instanceof RestTouristDto)
                || entity instanceof ShopTourist && !(dto instanceof ShopTouristDto)
                || entity instanceof Child && !(dto instanceof ChildDto)) {
            throw new IllegalArgumentException("Tourist type cannot be changed via update");
        }

        if (dto.firstName() != null) entity.setFirstName(dto.firstName());
        if (dto.lastName() != null) entity.setLastName(dto.lastName());
        if (dto.middleName() != null) entity.setMiddleName(dto.middleName());
        if (dto.gender() != null) entity.setGender(dto.gender());
        if (dto.birthDate() != null) entity.setBirthDate(dto.birthDate());
        if (dto.passportNumber() != null) entity.setPassportNumber(dto.passportNumber());

        switch (dto) {
            case RestTouristDto r -> {
                RestTourist e = (RestTourist) entity;
                if (r.preferredExcursionType() != null)
                    e.setPreferredExcursionType(r.preferredExcursionType());
            }
            case ShopTouristDto s -> {
                ShopTourist e = (ShopTourist) entity;
                if (s.cargoLimitKg() != null) e.setCargoLimitKg(s.cargoLimitKg());
                if (s.preferredCargoType() != null) e.setPreferredCargoType(s.preferredCargoType());
            }
            case ChildDto c -> {}
        }
    }


    private void fillCommon(Tourist e, String firstName, String lastName,
                            String middleName, ru.travelagency.enums.Gender gender,
                            java.time.LocalDate birthDate, String passportNumber) {
        e.setFirstName(firstName);
        e.setLastName(lastName);
        e.setMiddleName(middleName);
        e.setGender(gender);
        e.setBirthDate(birthDate);
        e.setPassportNumber(passportNumber);
    }

    private Set<Long> hotelIds(Tourist t) {
        if (t.getPreferredHotels() == null || t.getPreferredHotels().isEmpty()) {
            return Set.of();
        }
        return t.getPreferredHotels().stream()
                .map(Hotel::getId)
                .collect(Collectors.toCollection(HashSet::new));
    }
}
