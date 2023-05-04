package dev.armacki.echawatcher.repositories;

import dev.armacki.echawatcher.entities.Substance;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SubstanceRepository extends JpaRepository<Substance, Long> {
}
