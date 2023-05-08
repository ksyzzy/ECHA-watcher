package dev.armacki.echawatcher.repositories;

import dev.armacki.echawatcher.entities.Hazard;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface HazardRepository extends JpaRepository<Hazard, Long> {

    @Transactional
    List<Hazard> findAllByNameIn(List<String> names);
}
