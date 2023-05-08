package dev.armacki.echawatcher.repositories;

import dev.armacki.echawatcher.entities.Substance;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;

public interface SubstanceRepository extends JpaRepository<Substance, Long> {

    @Transactional
    List<Substance> findAllByIndexIn(Collection<String> indexes);

    @Transactional
    Page<Substance> findAllByIndexIn(List<String> indexes, Pageable pageable);
}
