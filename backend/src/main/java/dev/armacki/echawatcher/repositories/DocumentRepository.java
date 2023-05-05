package dev.armacki.echawatcher.repositories;

import dev.armacki.echawatcher.entities.Document;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DocumentRepository extends JpaRepository<Document, Long> {
}
