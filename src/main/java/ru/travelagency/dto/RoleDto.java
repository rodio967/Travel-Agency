package ru.travelagency.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RoleDto {

    private Long id;

    @NotBlank
    private String name;

    private String description;

    private boolean systemic;

    @Builder.Default
    private Set<Long> permissionIds = new HashSet<>();

    @Builder.Default
    private Set<String> permissionCodes = new HashSet<>();
}
