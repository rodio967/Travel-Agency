package ru.travelagency.mapper;

import org.springframework.stereotype.Component;
import ru.travelagency.domain.AppUser;
import ru.travelagency.domain.Role;
import ru.travelagency.dto.AppUserDto;

import java.util.HashSet;
import java.util.stream.Collectors;

@Component
public class AppUserMapper implements Mapper<AppUser, AppUserDto> {

    @Override
    public AppUserDto toDto(AppUser u) {
        if (u == null) return null;
        return AppUserDto.builder()
                .id(u.getId())
                .username(u.getUsername())
                .roleIds(u.getRoles().stream().map(Role::getId)
                        .collect(Collectors.toCollection(HashSet::new)))
                .roleNames(u.getRoles().stream().map(Role::getName)
                        .collect(Collectors.toCollection(HashSet::new)))
                .enabled(u.isEnabled())
                .build();
    }

    @Override
    public AppUser toEntity(AppUserDto dto) {
        if (dto == null) return null;
        AppUser u = new AppUser();
        u.setUsername(dto.getUsername());
        u.setEnabled(dto.isEnabled());
        return u;
    }

    @Override
    public void updateEntity(AppUser u, AppUserDto dto) {
        if (dto.getUsername() != null) u.setUsername(dto.getUsername());
        u.setEnabled(dto.isEnabled());
    }
}
