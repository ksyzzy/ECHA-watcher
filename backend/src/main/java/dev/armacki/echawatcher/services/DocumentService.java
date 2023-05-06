package dev.armacki.echawatcher.services;

import dev.armacki.echawatcher.dtos.DocumentDTO;
import dev.armacki.echawatcher.entities.Document;
import dev.armacki.echawatcher.entities.Substance;
import dev.armacki.echawatcher.helpers.DocumentAnalyzer;
import dev.armacki.echawatcher.helpers.HashService;
import dev.armacki.echawatcher.repositories.DocumentRepository;
import dev.armacki.echawatcher.repositories.FileSystemRepository;
import dev.armacki.echawatcher.repositories.SubstanceRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static dev.armacki.echawatcher.helpers.DocumentAnalyzer.DocumentStatus;

@AllArgsConstructor
@Service
public class DocumentService {

    private final Logger logger = LoggerFactory.getLogger(DocumentService.class);
    private final DocumentRepository documentRepository;
    private final SubstanceRepository substanceRepository;
    private final EntityMapperService entityMapperService;
    private final FileSystemRepository fileSystemRepository;

    private DocumentDTO documentToDTO(Document document) {
        return (DocumentDTO) entityMapperService.mapObject(document, DocumentDTO.class.getSimpleName());
    }

    public boolean handleFileUpload(MultipartFile file) throws IOException {
        String filename = file.getOriginalFilename();
        byte[] content = file.getBytes();
        String filePath = fileSystemRepository.saveFile(content, filename);
        if (filePath != null && !filePath.isBlank()) {
            String md5Checksum = HashService.generateChecksum(filePath);
            Document document = new Document(filename, md5Checksum, 1);
            documentRepository.save(document);
        }

        DocumentStatus documentStatus = DocumentAnalyzer.checkStructure(filePath);
        return documentStatus == DocumentStatus.VALID;
    }

    public void generateChangeSet() {


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
