package ru.travelagency.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Check;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "hotel_booking")
@Check(name = "chk_booking_dates", constraints = "check_out_date > check_in_date")
@Getter
@Setter
@NoArgsConstructor
public class HotelBooking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "tourist_id", nullable = false,
            foreignKey = @ForeignKey(name = "fk_hotel_booking_tourist"))
    private Tourist tourist;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "hotel_id", nullable = false,
            foreignKey = @ForeignKey(name = "fk_hotel_booking_hotel"))
    private Hotel hotel;

    @NotBlank
    @Column(name = "room_number", length = 20, nullable = false)
    private String roomNumber;

    @NotNull
    @Column(name = "check_in_date", nullable = false)
    private LocalDate checkInDate;

    @NotNull
    @Column(name = "check_out_date", nullable = false)
    private LocalDate checkOutDate;

    @Column(name = "cost", precision = 12, scale = 2)
    private BigDecimal cost;
}