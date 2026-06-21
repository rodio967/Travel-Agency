package ru.travelagency.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.travelagency.enums.FlightType;

import java.math.BigDecimal;
import java.time.LocalDate;


@Entity
@Table(name = "flight")
@Getter
@Setter
@NoArgsConstructor
public class Flight {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Column(name = "flight_number", nullable = false, length = 20)
    private String flightNumber;

    @NotNull
    @Column(name = "flight_date", nullable = false)
    private LocalDate flightDate;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "flight_type", nullable = false, length = 20)
    private FlightType flightType;

    @Positive
    @Column(name = "max_seats")
    private Integer maxSeats;

    @Column(name = "max_cargo_kg", precision = 10, scale = 2)
    private BigDecimal maxCargoKg;

    @Column(name = "airport_fees", precision = 12, scale = 2)
    private BigDecimal airportFees;
}
