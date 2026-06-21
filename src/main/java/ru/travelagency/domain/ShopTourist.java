package ru.travelagency.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;


@Entity
@Table(name = "shop_tourist")
@DiscriminatorValue("SHOP")
@PrimaryKeyJoinColumn(name = "id",
        foreignKey = @ForeignKey(name = "fk_shop_tourist_tourist"))
@Getter
@Setter
@NoArgsConstructor
public class ShopTourist extends Tourist {

    @Column(name = "cargo_limit_kg", precision = 10, scale = 2)
    private BigDecimal cargoLimitKg;

    @Column(name = "preferred_cargo_type", length = 100)
    private String preferredCargoType;

    @OneToMany(mappedBy = "tourist", fetch = FetchType.LAZY,
            cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Cargo> cargoList = new ArrayList<>();

    @Override
    public String getCategory() {
        return "SHOP";
    }
}
