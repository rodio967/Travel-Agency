package ru.travelagency.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.travelagency.domain.ExcursionAgency;
import ru.travelagency.dto.report.AgencyQualityDto;

import java.util.List;

@Repository
public interface ExcursionAgencyRepository extends JpaRepository<ExcursionAgency, Long>, JpaSpecificationExecutor<ExcursionAgency> {

    @Query("""
            select new ru.travelagency.dto.report.AgencyQualityDto(
                a.id,
                a.name,
                a.qualityRating,
                count(distinct e.id),
                count(b))
            from ExcursionAgency a
            left join Excursion e on e.agency = a
            left join ExcursionBooking b on b.excursion = e
            group by a.id, a.name, a.qualityRating
            order by a.qualityRating desc nulls last, count(b) desc""")
    List<AgencyQualityDto> topQualityAgencies();
}
