package ru.travelagency.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.travelagency.domain.Tourist;
import ru.travelagency.enums.TouristType;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface TouristRepository extends JpaRepository<Tourist, Long>, JpaSpecificationExecutor<Tourist> {

    List<Tourist> findAllByPassportNumber(String passportNumber);

    long countByGroupId(Long groupId);

    @Query("select t from Tourist t where TYPE(t) = :type")
    Page<Tourist> findAllByType(@Param("type") Class<? extends Tourist> type, Pageable pageable);

    @Query("select t from Tourist t where t.group.id = :groupId order by t.lastName")
    List<Tourist> findAllByGroupForCustoms(@Param("groupId") Long groupId);


    @Query("""
            select t from Tourist t
            where t.group.id = :groupId and TYPE(t) = :type
            order by t.lastName""")
    List<Tourist> findAllByGroupAndType(@Param("groupId") Long groupId,
                                        @Param("type") Class<? extends Tourist> type);

    @Query("""
            select count(t) from Tourist t
            where t.group.arrivalDate <= :to and t.group.departureDate >= :from""")
    long countByArrivalPeriod(@Param("from") LocalDate from,
                              @Param("to") LocalDate to);

    @Query("""
            select count(t) from Tourist t
            where t.group.arrivalDate <= :to and t.group.departureDate >= :from
            and TYPE(t) = :type""")
    long countByArrivalPeriodAndType(@Param("from") LocalDate from,
                                     @Param("to") LocalDate to,
                                     @Param("type") Class<? extends Tourist> type);

    @Query("""
            select TYPE(t), count(t) from Tourist t
            where t.group.arrivalDate <= :to and t.group.departureDate >= :from
            group by TYPE(t)""")
    List<Object[]> categoryShareInPeriod(@Param("from") LocalDate from,
                                         @Param("to") LocalDate to);

    @Query("select TYPE(t), count(t) from Tourist t group by TYPE(t)")
    List<Object[]> categoryShareTotal();
}
