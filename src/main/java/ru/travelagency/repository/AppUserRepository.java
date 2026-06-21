package ru.travelagency.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.travelagency.domain.AppUser;

import java.util.Optional;

@Repository
public interface AppUserRepository extends JpaRepository<AppUser, Long> {

    Optional<AppUser> findByUsername(String username);

    @Query("""
            select distinct u from AppUser u
            left join fetch u.roles r
            left join fetch r.permissions
            where u.username = :username""")
    Optional<AppUser> findByUsernameWithRolesAndPermissions(@Param("username") String username);

    boolean existsByUsername(String username);
}
