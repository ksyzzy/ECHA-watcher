package dev.armacki.echawatcher.services;

import dev.armacki.echawatcher.dtos.HazardDTO;
import dev.armacki.echawatcher.entities.Document;
import dev.armacki.echawatcher.entities.Hazard;
import dev.armacki.echawatcher.helpers.DocumentAnalyzer;
import dev.armacki.echawatcher.repositories.HazardRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static dev.armacki.echawatcher.helpers.DocumentAnalyzer.SubstanceData;
import static dev.armacki.echawatcher.helpers.DocumentAnalyzer.HazardData;

@AllArgsConstructor
@Service
public class HazardService {

    private final EntityMapperService entityMapperService;
    private final HazardRepository hazardRepository;

    private HazardDTO documentToDTO(Document document) {
        return (HazardDTO) entityMapperService.mapObject(document, HazardDTO.class.getSimpleName());
    }

    public void crossCheckHazards(HashMap<String, SubstanceData> mappedData,
                                  HashMap<String, Long> existingHazards)
    {
        List<HazardData> hazards = mappedData.values().stream()
                .map(SubstanceData::getHazards).flatMap(List::stream).toList();

        for (HazardData hazard : hazards) {
            if (existingHazards.containsKey(hazard.getName())) {
                hazard.setDatabaseRecordId(existingHazards.get(hazard.getName()));
            }
        }
    }
}
