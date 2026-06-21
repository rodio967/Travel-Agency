package ru.travelagency.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.travelagency.domain.HotelBooking;
import ru.travelagency.domain.Tourist;
import ru.travelagency.dto.report.HotelOccupancyDto;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface HotelBookingRepository extends JpaRepository<HotelBooking, Long>, JpaSpecificationExecutor<HotelBooking> {

    List<HotelBooking> findAllByTouristId(Long touristId);
    List<HotelBooking> findAllByHotelId(Long hotelId);

    @Query("""
            select new ru.travelagency.dto.report.HotelOccupancyDto(
                b.hotel.id,
                b.hotel.name,
                count(distinct b.roomNumber),
                count(distinct b.tourist.id))
            from HotelBooking b
            where b.checkInDate <= :to and b.checkOutDate >= :from
            group by b.hotel.id, b.hotel.name
            order by count(distinct b.tourist.id) desc""")
    List<HotelOccupancyDto> occupancyInPeriod(@Param("from") LocalDate from,
                                              @Param("to") LocalDate to);

    @Query("""
            select distinct b.tourist from HotelBooking b
            where b.hotel.id = :hotelId
            and (:type is null or TYPE(b.tourist) = :type)
            and (cast(:from as date) is null or b.checkOutDate >= :from)
            and (cast(:to as date) is null or b.checkInDate <= :to)
            order by b.tourist.lastName""")
    List<Tourist> findGuestsByHotel(@Param("hotelId") Long hotelId,
                                    @Param("type") Class<? extends Tourist> type,
                                    @Param("from") LocalDate from,
                                    @Param("to") LocalDate to);
}
