package ru.travelagency.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.travelagency.domain.Flight;
import ru.travelagency.domain.Tourist;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface FlightRepository extends JpaRepository<Flight, Long>, JpaSpecificationExecutor<Flight> {

    Optional<Flight> findByFlightNumberAndFlightDate(String flightNumber, LocalDate flightDate);

    @Query("""
            select distinct t from Tourist t
            where t.group.arrivalFlight.id = :flightId
            or t.group.departureFlight.id = :flightId
            order by t.lastName""")
    List<Tourist> findTouristsByFlight(@Param("flightId") Long flightId);
}
