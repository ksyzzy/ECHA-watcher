package dev.armacki.echawatcher.services;

import dev.armacki.echawatcher.dtos.HazardDTO;
import dev.armacki.echawatcher.entities.Document;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@AllArgsConstructor
@Service
public class HazardService {

    private final EntityMapperService entityMapperService;

    private HazardDTO documentToDTO(Document document) {
        return (HazardDTO) entityMapperService.mapObject(document, HazardDTO.class.getSimpleName());
    }
}
