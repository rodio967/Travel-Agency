package ru.travelagency.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import ru.travelagency.domain.TouristGroup;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface TouristGroupRepository extends JpaRepository<TouristGroup, Long>, JpaSpecificationExecutor<TouristGroup> {

    List<TouristGroup> findAllByArrivalDateBetween(LocalDate from, LocalDate to);
}
