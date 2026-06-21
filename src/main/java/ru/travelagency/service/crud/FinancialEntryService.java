package ru.travelagency.service.crud;

import org.springframework.stereotype.Service;
import ru.travelagency.domain.FinancialEntry;
import ru.travelagency.domain.Tourist;
import ru.travelagency.domain.TouristGroup;
import ru.travelagency.dto.core.FinancialEntryDto;
import ru.travelagency.exception.BusinessRuleException;
import ru.travelagency.exception.NotFoundException;
import ru.travelagency.mapper.FinancialEntryMapper;
import ru.travelagency.repository.FinancialEntryRepository;
import ru.travelagency.repository.TouristGroupRepository;
import ru.travelagency.repository.TouristRepository;

@Service
public class FinancialEntryService extends AbstractCrudService<FinancialEntry, Long, FinancialEntryDto> {

    private final TouristGroupRepository groupRepository;
    private final TouristRepository touristRepository;

    public FinancialEntryService(FinancialEntryRepository repository,
                                 FinancialEntryMapper mapper,
                                 TouristGroupRepository groupRepository,
                                 TouristRepository touristRepository) {
        super(repository, mapper, FinancialEntry.class);
        this.groupRepository = groupRepository;
        this.touristRepository = touristRepository;
    }

    @Override
    protected void beforeCreate(FinancialEntry entity, FinancialEntryDto dto) {
        entity.setGroup(loadGroup(dto.getGroupId()));
        entity.setTourist(resolveTourist(dto, entity.getGroup().getId()));
    }

    @Override
    protected void beforeUpdate(FinancialEntry entity, FinancialEntryDto dto) {
        if (dto.getGroupId() != null) entity.setGroup(loadGroup(dto.getGroupId()));

        entity.setTourist(resolveTourist(dto, entity.getGroup().getId()));
    }

    private TouristGroup loadGroup(Long id) {
        return groupRepository.findById(id)
                .orElseThrow(() -> NotFoundException.of(TouristGroup.class, id));
    }


    private Tourist resolveTourist(FinancialEntryDto dto, Long groupId) {
        if (dto.getTouristId() == null) return null;

        Tourist tourist = touristRepository.findById(dto.getTouristId())
                .orElseThrow(() -> NotFoundException.of(Tourist.class, dto.getTouristId()));
        if (tourist.getGroup() == null || !tourist.getGroup().getId().equals(groupId)) {
            throw new BusinessRuleException("Tourist " + dto.getTouristId() + " does not belong to group " + groupId);
        }
        return tourist;
    }
}