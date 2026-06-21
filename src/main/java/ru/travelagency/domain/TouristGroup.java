package ru.travelagency.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Check;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "tourist_group")
@Check(name = "chk_group_dates", constraints = "departure_date >= arrival_date")
@Getter
@Setter
@NoArgsConstructor
public class TouristGroup {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Column(name = "name", nullable = false, length = 200)
    private String name;

    @NotNull
    @Column(name = "arrival_date", nullable = false)
    private LocalDate arrivalDate;

    @NotNull
    @Column(name = "departure_date", nullable = false)
    private LocalDate departureDate;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "arrival_flight_id", nullable = false,
            foreignKey = @ForeignKey(name = "fk_group_arrival_flight"))
    private Flight arrivalFlight;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "departure_flight_id", nullable = false,
            foreignKey = @ForeignKey(name = "fk_group_departure_flight"))
    private Flight departureFlight;

    @OneToMany(mappedBy = "group", fetch = FetchType.LAZY,
            cascade = CascadeType.PERSIST)
    private List<Tourist> tourists = new ArrayList<>();

    @OneToMany(mappedBy = "group", fetch = FetchType.LAZY,
            cascade = CascadeType.ALL, orphanRemoval = true)
    private List<FinancialEntry> financialEntries = new ArrayList<>();
}
