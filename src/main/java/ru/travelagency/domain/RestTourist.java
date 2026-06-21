package ru.travelagency.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "rest_tourist")
@DiscriminatorValue("REST")
@PrimaryKeyJoinColumn(name = "id",
        foreignKey = @ForeignKey(name = "fk_rest_tourist_tourist"))
@Getter
@Setter
@NoArgsConstructor
public class RestTourist extends Tourist {

    @Column(name = "preferred_excursion_type", length = 100)
    private String preferredExcursionType;

    @Override
    public String getCategory() {
        return "REST";
    }
}
