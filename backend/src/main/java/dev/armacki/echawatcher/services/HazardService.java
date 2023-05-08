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

    public void crossCheckHazards(HashMap<String, SubstanceData> mappedData) {
        HashMap<String, Long> existingHazards = new HashMap<>();
        HashMap<String, HazardData> mappedHazards = new HashMap<>();
        List<String> hazardNames = new ArrayList<>();
        for (SubstanceData sd : mappedData.values()) {
            for (HazardData hd : sd.getHazards()) {
                HazardData result = mappedHazards.putIfAbsent(hd.getName(), hd);
                if (result != null)
                    hazardNames.add(hd.getName());
            }
        }

        List<Hazard> hazardsDB = hazardRepository.findAllByNameIn(hazardNames);
        for (Hazard hazard : hazardsDB) {
            existingHazards.putIfAbsent(hazard.getName(), hazard.getId());
        }

        for (Map.Entry<String, HazardData> entry : mappedHazards.entrySet()) {
            if (existingHazards.containsKey(entry.getKey())) {
                mappedHazards.get(entry.getKey()).setDatabaseRecordId(existingHazards.get(entry.getKey()));
                mappedHazards.get(entry.getKey()).setOperation(DocumentAnalyzer.DatabaseOperation.NO_CHANGE);
            }
        }
    }
}
