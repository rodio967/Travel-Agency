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
public class AppUserDto {

    private Long id;

    @NotBlank
    private String username;

    private String password;

    @Builder.Default
    private Set<Long> roleIds = new HashSet<>();

    @Builder.Default
    private Set<String> roleNames = new HashSet<>();

    private boolean enabled;
}
