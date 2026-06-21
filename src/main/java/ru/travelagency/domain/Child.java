package ru.travelagency.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Check;

@Entity
@Table(name = "child")
@Check(name = "chk_child_not_self_parent", constraints = "parent_id <> id")
@DiscriminatorValue("CHILD")
@PrimaryKeyJoinColumn(name = "id",
        foreignKey = @ForeignKey(name = "fk_child_tourist"))
@Getter
@Setter
@NoArgsConstructor
public class Child extends Tourist {

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "parent_id", nullable = false,
            foreignKey = @ForeignKey(name = "fk_child_parent"))
    private Tourist parent;

    @Override
    public String getCategory() {
        return "CHILD";
    }
}
