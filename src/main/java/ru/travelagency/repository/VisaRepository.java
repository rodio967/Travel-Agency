package ru.travelagency.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.travelagency.domain.Visa;

import java.util.Optional;

@Repository
public interface VisaRepository extends JpaRepository<Visa, Long> {

    Optional<Visa> findByTouristId(Long touristId);

    boolean existsByVisaNumber(String visaNumber);
}
