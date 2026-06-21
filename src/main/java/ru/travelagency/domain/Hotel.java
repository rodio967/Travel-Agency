package ru.travelagency.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Table(name = "hotel")
@Getter
@Setter
@NoArgsConstructor
public class Hotel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Column(name = "name", nullable = false, length = 200)
    private String name;

    @NotBlank
    @Column(name = "address", nullable = false, length = 500)
    private String address;

    @Min(1) @Max(5)
    @Column(name = "stars")
    private Integer stars;

    @Column(name = "price_per_night", precision = 10, scale = 2)
    private BigDecimal pricePerNight;
}
