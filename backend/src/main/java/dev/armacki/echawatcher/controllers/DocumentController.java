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

//    @GetMapping("/")
//    public ResponseEntity<List<DocumentDTO>> getDocuments(@RequestParam("page") String page,
//                                                          @RequestParam("size") String size)
//    {
//
//    }
}
