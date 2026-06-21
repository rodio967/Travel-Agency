package ru.travelagency.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.travelagency.domain.Cargo;
import ru.travelagency.dto.report.FlightLoadDto;
import ru.travelagency.dto.report.PeriodTotals;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface CargoRepository extends JpaRepository<Cargo, Long>, JpaSpecificationExecutor<Cargo> {

    List<Cargo> findAllByTouristId(Long touristId);
    List<Cargo> findAllByFlightId(Long flightId);

    @Query("""
            select new ru.travelagency.dto.report.FlightLoadDto(
                f.id, f.flightNumber, f.flightDate,
                count(c),
                coalesce(sum(c.placesCount), 0L),
                coalesce(sum(c.weightKg), 0),
                coalesce(sum(c.volumetricWeight), 0))
            from Flight f
            left join Cargo c on c.flight = f
            where f.id = :flightId
            group by f.id, f.flightNumber, f.flightDate""")
    FlightLoadDto getFlightLoad(@Param("flightId") Long flightId);


    @Query("""
            select new ru.travelagency.dto.report.PeriodTotals(
                coalesce(sum(c.placesCount), 0L),
                coalesce(sum(c.weightKg), 0))
            from Cargo c
            where c.flight.flightDate between :from and :to""")
    PeriodTotals sumPlacesAndWeightInPeriod(@Param("from") LocalDate from,
                                            @Param("to") LocalDate to);

    @Query("""
            select c.flight.flightType, count(distinct c.flight.id)
            from Cargo c
            where c.flight.flightDate between :from and :to
            group by c.flight.flightType""")
    List<Object[]> flightTypeBreakdownInPeriod(@Param("from") LocalDate from, @Param("to") LocalDate to);

    @Query("""
            select coalesce(c.cargoType, 'UNKNOWN'),
                count(c),
                coalesce(sum(c.weightKg), 0)
            from Cargo c
            group by c.cargoType
            order by sum(c.weightKg) desc nulls last""")
    List<Object[]> statsByCargoType();


    @Query("select coalesce(sum(c.weightKg), 0) from Cargo c")
    BigDecimal totalCargoWeight();
}
