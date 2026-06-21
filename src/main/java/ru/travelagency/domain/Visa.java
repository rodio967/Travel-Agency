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
@Table(name = "visa", uniqueConstraints = {
        @UniqueConstraint(name = "uk_visa_number", columnNames = "visa_number"),
        @UniqueConstraint(name = "uk_visa_tourist", columnNames = "tourist_id")
})
@Check(name = "chk_visa_dates", constraints = "expiry_date > issue_date")
@Getter
@Setter
@NoArgsConstructor
public class Visa {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "tourist_id", nullable = false,
            foreignKey = @ForeignKey(name = "fk_visa_tourist"))
    private Tourist tourist;

    @NotBlank
    @Column(name = "visa_number", nullable = false, length = 50)
    private String visaNumber;

    @NotNull
    @Column(name = "issue_date", nullable = false)
    private LocalDate issueDate;

    @NotNull
    @Column(name = "expiry_date", nullable = false)
    private LocalDate expiryDate;

    @Column(name = "cost", precision = 10, scale = 2)
    private BigDecimal cost;
}
