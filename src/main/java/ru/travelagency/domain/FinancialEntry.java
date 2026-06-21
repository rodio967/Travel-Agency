package ru.travelagency.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Check;
import ru.travelagency.enums.EntryType;
import ru.travelagency.enums.FinancialCategory;

import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "financial_entry")
@Check(name = "chk_fin_amount", constraints = "amount >= 0")
@Getter
@Setter
@NoArgsConstructor
public class FinancialEntry {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "group_id", nullable = false,
            foreignKey = @ForeignKey(name = "fk_financial_entry_group"))
    private TouristGroup group;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tourist_id",
            foreignKey = @ForeignKey(name = "fk_financial_entry_tourist"))
    private Tourist tourist;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "entry_type", nullable = false, length = 20)
    private EntryType entryType;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "category", nullable = false, length = 30)
    private FinancialCategory category;

    @NotNull
    @Column(name = "amount", nullable = false, precision = 14, scale = 2)
    private BigDecimal amount;

    @NotNull
    @Column(name = "entry_date", nullable = false)
    private LocalDate entryDate;

    @Column(name = "description", length = 500)
    private String description;
}