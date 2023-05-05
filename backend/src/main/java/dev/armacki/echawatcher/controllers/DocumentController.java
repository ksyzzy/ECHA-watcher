package dev.armacki.echawatcher.controllers;

import dev.armacki.echawatcher.dtos.DocumentDTO;
import dev.armacki.echawatcher.services.DocumentService;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/api/documents")
public class DocumentController {

    private DocumentService documentService;

    @GetMapping("/{id}")
    public ResponseEntity<Object> getDocumentById(@PathVariable long id) {
        try {
            DocumentDTO document = documentService.getDocumentById(id);
            return new ResponseEntity<>(document, HttpStatus.OK);
        } catch (EntityNotFoundException ex) {
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping()
    public Object getDocumentsPageSize(
            @RequestParam(name = "page", required = false) String page,
            @RequestParam(name = "size", required = false) String size)
    {
        int pageValue = 0;
        int sizeValue = 50;
        try {
            pageValue = Integer.parseInt(page);
        } catch (NumberFormatException ex) {
            if (page != null)
                return new ResponseEntity<>("Invalid page format", HttpStatus.BAD_REQUEST);
        }
        try {
            sizeValue = Integer.parseInt(size);
        } catch (NumberFormatException ex) {
            if (size != null)
                return new ResponseEntity<>("Invalid size format", HttpStatus.BAD_REQUEST);
        }

        List<DocumentDTO> documents = documentService.getDocuments(pageValue, sizeValue);
        return new ResponseEntity<>(documents, HttpStatus.OK);
    }
}
