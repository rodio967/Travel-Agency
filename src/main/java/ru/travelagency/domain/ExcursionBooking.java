package ru.travelagency.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "excursion_booking", uniqueConstraints = {
        @UniqueConstraint(name = "uk_booking_tourist_excursion",
                columnNames = {"tourist_id", "excursion_id"})
})
@Getter
@Setter
@NoArgsConstructor
public class ExcursionBooking {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "tourist_id", nullable = false,
            foreignKey = @ForeignKey(name = "fk_excursion_booking_tourist"))
    private Tourist tourist;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "excursion_id", nullable = false,
            foreignKey = @ForeignKey(name = "fk_excursion_booking_excursion"))
    private Excursion excursion;

    @Column(name = "booked_at")
    private LocalDateTime bookedAt;
}
