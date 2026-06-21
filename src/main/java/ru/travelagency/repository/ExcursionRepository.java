package ru.travelagency.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import ru.travelagency.domain.Excursion;

import java.util.List;

@Repository
public interface ExcursionRepository extends JpaRepository<Excursion, Long>, JpaSpecificationExecutor<Excursion> {

    List<Excursion> findAllByAgencyId(Long agencyId);
}
