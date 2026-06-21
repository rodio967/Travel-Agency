package ru.travelagency.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;


@Entity
@Table(name = "excursion_agency")
@Getter
@Setter
@NoArgsConstructor
public class ExcursionAgency {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Column(name = "name", nullable = false, length = 200)
    private String name;

    @Column(name = "contact", length = 200)
    private String contact;

    @DecimalMin("0.0") @DecimalMax("10.0")
    @Column(name = "quality_rating", precision = 3, scale = 1)
    private BigDecimal qualityRating;
}
