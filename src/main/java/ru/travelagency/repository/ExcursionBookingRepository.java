package ru.travelagency.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.travelagency.domain.ExcursionBooking;
import ru.travelagency.dto.report.ExcursionPopularityDto;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ExcursionBookingRepository extends JpaRepository<ExcursionBooking, Long>, JpaSpecificationExecutor<ExcursionBooking> {

    List<ExcursionBooking> findAllByTouristId(Long touristId);
    List<ExcursionBooking> findAllByExcursionId(Long excursionId);

    @Query("""
            select count(distinct b.tourist.id) from ExcursionBooking b
            where b.excursion.excursionDate between :from and :to""")
    long countDistinctTouristsInPeriod(@Param("from") LocalDate from,
                                       @Param("to") LocalDate to);

    @Query("""
            select new ru.travelagency.dto.report.ExcursionPopularityDto(
                b.excursion.id,
                b.excursion.name,
                b.excursion.agency.id,
                b.excursion.agency.name,
                count(b))
            from ExcursionBooking b
            group by b.excursion.id, b.excursion.name, b.excursion.agency.id, b.excursion.agency.name
            order by count(b) desc""")
    List<ExcursionPopularityDto> mostPopularExcursions();
}
