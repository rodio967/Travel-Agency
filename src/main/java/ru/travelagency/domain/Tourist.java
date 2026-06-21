package ru.travelagency.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Check;
import ru.travelagency.enums.Gender;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "tourist")
@Check(name = "chk_tourist_adult_has_passport",
        constraints = "tourist_type = 'CHILD' OR passport_number IS NOT NULL")
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn(name = "tourist_type", discriminatorType = DiscriminatorType.STRING, length = 20)
@Getter
@Setter
@NoArgsConstructor
public abstract class Tourist {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    @Column(name = "first_name", nullable = false, length = 100)
    private String firstName;

    @NotBlank
    @Column(name = "last_name", nullable = false, length = 100)
    private String lastName;

    @Column(name = "middle_name", length = 100)
    private String middleName;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "gender", nullable = false, length = 10)
    private Gender gender;

    @NotNull
    @Column(name = "birth_date", nullable = false)
    private LocalDate birthDate;

    @Column(name = "passport_number", length = 30)
    private String passportNumber;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "group_id", nullable = false,
            foreignKey = @ForeignKey(name = "fk_tourist_group"))
    private TouristGroup group;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "tourist_preferred_hotels",
            joinColumns = @JoinColumn(name = "tourist_id",
                    foreignKey = @ForeignKey(name = "fk_tph_tourist")),
            inverseJoinColumns = @JoinColumn(name = "hotel_id",
                    foreignKey = @ForeignKey(name = "fk_tph_hotel")))
    private Set<Hotel> preferredHotels = new HashSet<>();

    public abstract String getCategory();
}
