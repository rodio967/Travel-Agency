package ru.travelagency.mapper;

import org.springframework.stereotype.Component;
import ru.travelagency.domain.Cargo;
import ru.travelagency.dto.core.CargoDto;

@Component
public class CargoMapper implements Mapper<Cargo, CargoDto> {

    @Override
    public CargoDto toDto(Cargo c) {
        if (c == null) return null;
        return CargoDto.builder()
                .id(c.getId())
                .touristId(c.getTourist() != null ? c.getTourist().getId() : null)
                .flightId(c.getFlight() != null ? c.getFlight().getId() : null)
                .marking(c.getMarking())
                .cargoType(c.getCargoType())
                .placesCount(c.getPlacesCount())
                .weightKg(c.getWeightKg())
                .volumetricWeight(c.getVolumetricWeight())
                .packagingCost(c.getPackagingCost())
                .insuranceCost(c.getInsuranceCost())
                .totalCost(c.getTotalCost())
                .build();
    }

    @Override
    public Cargo toEntity(CargoDto dto) {
        if (dto == null) return null;
        Cargo c = new Cargo();
        c.setMarking(dto.getMarking());
        c.setCargoType(dto.getCargoType());
        c.setPlacesCount(dto.getPlacesCount());
        c.setWeightKg(dto.getWeightKg());
        c.setVolumetricWeight(dto.getVolumetricWeight());
        c.setPackagingCost(dto.getPackagingCost());
        c.setInsuranceCost(dto.getInsuranceCost());
        c.setTotalCost(dto.getTotalCost());
        return c;
    }

    @Override
    public void updateEntity(Cargo c, CargoDto dto) {
        if (dto.getMarking() != null) c.setMarking(dto.getMarking());
        if (dto.getCargoType() != null) c.setCargoType(dto.getCargoType());
        if (dto.getPlacesCount() != null) c.setPlacesCount(dto.getPlacesCount());
        if (dto.getWeightKg() != null) c.setWeightKg(dto.getWeightKg());
        if (dto.getVolumetricWeight() != null) c.setVolumetricWeight(dto.getVolumetricWeight());
        if (dto.getPackagingCost() != null) c.setPackagingCost(dto.getPackagingCost());
        if (dto.getInsuranceCost() != null) c.setInsuranceCost(dto.getInsuranceCost());
        if (dto.getTotalCost() != null) c.setTotalCost(dto.getTotalCost());
    }
}
