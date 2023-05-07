package dev.armacki.echawatcher.repositories;

import dev.armacki.echawatcher.entities.Substance;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;

public interface SubstanceRepository extends JpaRepository<Substance, Long> {

    List<Substance> findAllByIndexIn(Collection<String> index);
    Page<Substance> findAllByIndexIn(List<String> indexes, Pageable pageable);
}
