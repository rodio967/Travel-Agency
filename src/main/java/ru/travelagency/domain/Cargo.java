package ru.travelagency.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Table(name = "cargo")
@Getter
@Setter
@NoArgsConstructor
public class Cargo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "tourist_id", nullable = false,
            foreignKey = @ForeignKey(name = "fk_cargo_tourist"))
    private Tourist tourist;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "flight_id", nullable = false,
            foreignKey = @ForeignKey(name = "fk_cargo_flight"))
    private Flight flight;

    @Column(name = "marking", length = 100)
    private String marking;

    @Column(name = "cargo_type", length = 100)
    private String cargoType;

    @Positive
    @Column(name = "places_count")
    private Integer placesCount;

    @PositiveOrZero
    @Column(name = "weight_kg", precision = 10, scale = 2)
    private BigDecimal weightKg;

    @PositiveOrZero
    @Column(name = "volumetric_weight", precision = 10, scale = 2)
    private BigDecimal volumetricWeight;

    @PositiveOrZero
    @Column(name = "packaging_cost", precision = 10, scale = 2)
    private BigDecimal packagingCost;

    @PositiveOrZero
    @Column(name = "insurance_cost", precision = 10, scale = 2)
    private BigDecimal insuranceCost;

    @PositiveOrZero
    @Column(name = "total_cost", precision = 12, scale = 2)
    private BigDecimal totalCost;
}
