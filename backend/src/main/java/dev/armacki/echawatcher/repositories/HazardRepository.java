package dev.armacki.echawatcher.repositories;

import dev.armacki.echawatcher.entities.Hazard;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HazardRepository extends JpaRepository<Hazard, Long> {
}
