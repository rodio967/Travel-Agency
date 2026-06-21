package ru.travelagency.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.travelagency.domain.ShopTourist;

@Repository
public interface ShopTouristRepository extends JpaRepository<ShopTourist, Long> {
}
