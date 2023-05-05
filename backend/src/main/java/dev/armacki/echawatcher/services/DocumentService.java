package dev.armacki.echawatcher.services;

import dev.armacki.echawatcher.dtos.DocumentDTO;
import dev.armacki.echawatcher.entities.Document;
import dev.armacki.echawatcher.repositories.DocumentRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@AllArgsConstructor
@Service
public class DocumentService {

    private final Logger logger = LoggerFactory.getLogger(DocumentService.class);
    private final DocumentRepository documentRepository;
    private final EntityMapperService entityMapperService;

    private DocumentDTO documentToDTO(Document document) {
        return (DocumentDTO) entityMapperService.mapObject(document, DocumentDTO.class.getSimpleName());
    }

    public DocumentDTO getDocumentById(long id) {
        Optional<Document> entity = documentRepository.findById(id);
        if (entity.isEmpty()) {
            logger.warn(String.format("Document with id %d does not exist", id));
            throw new EntityNotFoundException(String.format("Document with id %d does not exist", id));
        }

        return documentToDTO(entity.get());
    }

    public List<DocumentDTO> getDocuments(int page, int size) {
        PageRequest pageReq = PageRequest.of(page, size);

        return documentRepository.findAll(pageReq).stream()
                .map(this::documentToDTO)
                .toList();
    }

    public DocumentDTO addDocument(Document document) {
        Document savedDocument = documentRepository.save(document);
        return documentToDTO(savedDocument);
    }

    public DocumentDTO updateDocument(Document document, long id) {
        if (!documentRepository.existsById(id))
            throw new EntityNotFoundException(String.format("Document with id %d does not exist", id));

        Optional<Document> entity = documentRepository.findById(id);
        document.setId(id);
        Document result = documentRepository.save(document);

        return documentToDTO(result);
    }

    public boolean deleteDocumentById(long id) {
        if (!documentRepository.existsById(id))
            return false;

        documentRepository.deleteById(id);
        return true;
    }
}
