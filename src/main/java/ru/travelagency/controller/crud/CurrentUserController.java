package ru.travelagency.controller.crud;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RestController
@RequestMapping("/api/me")
public class CurrentUserController {

    @GetMapping
    public CurrentUserResponse me(Authentication auth) {
        List<String> roles = auth.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .filter(a -> a.startsWith("ROLE_"))
                .toList();

        List<String> permissions = auth.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .filter(a -> !a.startsWith("ROLE_"))
                .toList();
        return new CurrentUserResponse(auth.getName(), roles, permissions);
    }

    public record CurrentUserResponse(
            String username,
            List<String> roles,
            List<String> permissions
    ) {}
}
