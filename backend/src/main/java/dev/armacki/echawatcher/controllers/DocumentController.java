package dev.armacki.echawatcher.controllers;

import dev.armacki.echawatcher.dtos.DocumentDTO;
import dev.armacki.echawatcher.entities.Hazard;
import dev.armacki.echawatcher.entities.Substance;
import dev.armacki.echawatcher.entities.SubstanceHazard;
import dev.armacki.echawatcher.repositories.HazardRepository;
import dev.armacki.echawatcher.repositories.SubstanceHazardRepository;
import dev.armacki.echawatcher.repositories.SubstanceRepository;
import dev.armacki.echawatcher.services.DocumentService;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/api/documents")
public class DocumentController {

    private DocumentService documentService;
    private SubstanceRepository substanceRepository;
    private HazardRepository hazardRepository;
    private SubstanceHazardRepository substanceHazardRepository;

    @GetMapping("/{id}")
    public ResponseEntity<Object> getDocumentById(@PathVariable long id) {
        try {
            DocumentDTO document = documentService.getDocumentById(id);
            return new ResponseEntity<>(document, HttpStatus.OK);
        } catch (EntityNotFoundException ex) {
            return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/populate")
    public Object populateDatabase() {
        Substance substance = new Substance();
        substance.setIndex("001-001-00-9");
        substance.setName("hydrogen");
        substance.setEcNo("215-605-7");
        substance.setCasNo("1333-74-0");
        substance = substanceRepository.save(substance);

        Hazard hazard = new Hazard();
        hazard.setName("Flam. Gas 1");
        hazard.setCode("H220");
        hazard = hazardRepository.save(hazard);

        SubstanceHazard sh = new SubstanceHazard();
        sh.setHazard(hazard);
        sh.setSubstance(substance);
        substanceHazardRepository.save(sh);
        return new ResponseEntity<>("Populated", HttpStatus.OK);
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

    @PostMapping()
    public Object postNewDocument(@RequestParam("file") MultipartFile file) throws IOException {
        String filePath = documentService.handleFileUpload(file);
        boolean result = documentService.checkIfDocumentValid(filePath);
        if (!result)
            return new ResponseEntity<>("Niedobrze", HttpStatus.BAD_REQUEST);

        String jsonFilename = documentService.generateChangeSet(filePath);
        return new ResponseEntity<>(jsonFilename, HttpStatus.OK);
    }
}
