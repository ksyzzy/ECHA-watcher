package dev.armacki.echawatcher.helpers;

import com.fasterxml.jackson.databind.ObjectMapper;
import dev.armacki.echawatcher.entities.Hazard;
import dev.armacki.echawatcher.entities.Substance;
import dev.armacki.echawatcher.entities.SubstanceHazard;
import dev.armacki.echawatcher.repositories.SubstanceRepository;
import dev.armacki.echawatcher.services.HazardService;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.hibernate.dialect.Database;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.Serializable;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

@AllArgsConstructor
@Component
public class DocumentAnalyzer {

    @Getter @Setter
    public static class SubstanceData implements Serializable {
        private long databaseRecordId;
        private DatabaseOperation operation = DatabaseOperation.NO_CHANGE;
        private String index;
        private String name;
        private String ecNo;
        private String casNo;
        private List<HazardData> hazards = new ArrayList<>();
    }

    @Getter @Setter
    @RequiredArgsConstructor
    public static class HazardData implements Serializable {
        private long databaseRecordId;
        private DatabaseOperation operation = DatabaseOperation.ADD;
        private final String name;
        private final String code;
    }

    private final Logger logger = LoggerFactory.getLogger(DocumentAnalyzer.class);

    private final SubstanceRepository substanceRepository;
    private final HazardService hazardService;
    private final int BATCH_SIZE = 1000;
    private final List<XLSXCellCoord> requiredCells = new ArrayList<>(Arrays.asList(
            new XLSXCellCoord(4, 0, "Index No"),
            new XLSXCellCoord(4, 1, "International Chemical Identification"),
            new XLSXCellCoord(4, 2, "EC No"),
            new XLSXCellCoord(4, 3, "CAS No"),
            new XLSXCellCoord(5, 4, "Hazard Class and Category Code(s)"),
            new XLSXCellCoord(5, 5, "Hazard Statement Code(s)")
    ));

    private final HashMap<String, Integer> COLUMNS = new HashMap<>(Map.of(
            "index", 0,
            "substance", 1,
            "ec", 2,
            "cas", 3,
            "hazards", 4,
            "codes", 5
    ));

    public enum DocumentStatus {
        VALID,
        INVALID
    }

    public enum DatabaseOperation {
        ADD("ADD"),
        REMOVE("REMOVE"),
        NO_CHANGE("NO_CHANGE");

        private DatabaseOperation(String value) {
        }
    }

    private List<HazardData> matchHazardsWithCodes(Cell hazardCell, Cell codesCell) throws Exception {
        List<String> hazards;
        String[] hazardArray = hazardCell.getStringCellValue().split(System.lineSeparator());
        hazards = Arrays.stream(hazardArray)
                .filter(el -> !el.isBlank())
                .collect(Collectors.toList());

        List<String> codes;
        String[] codesArray = codesCell.getStringCellValue().split(System.lineSeparator());
        codes = Arrays.stream(codesArray)
                .filter(el -> !el.isBlank())
                .collect(Collectors.toList());

        List<HazardData> result = new ArrayList<>();
        if (hazards.contains("Press. Gas")) {
            hazards.remove("Press. Gas");
            result.add(new HazardData("Press. Gas", null));
        }
        if (hazards.size() != codes.size()) {
            throw new Exception("Could not match hazard names with codes");
        }
        for (int i = 0; i < hazards.size(); i++) {
            result.add(new HazardData(hazards.get(i), codes.get(i)));
        }

        return result;
    }

    private void filterChangesWithDatabaseEntities(List<SubstanceData> data,
                                                   HashMap<String, SubstanceData> mappedData,
                                                   List<String> indexes)
    {
        List<Substance> substancesDB = substanceRepository.findAllByIndexIn(indexes);
        for (Substance substanceDB : substancesDB) {
            SubstanceData substanceData = mappedData.get(substanceDB.getIndex());
            if (substanceData == null) {
                SubstanceData sd = new SubstanceData();
                sd.setIndex(substanceDB.getIndex());
                sd.setDatabaseRecordId(substanceDB.getId());
                sd.setOperation(DatabaseOperation.REMOVE);
                data.add(sd);
                continue;
            }

            substanceData.setDatabaseRecordId(substanceDB.getId());

            List<HazardData> substanceDataHazards = substanceData.getHazards();
            HashMap<String, HazardData> mappedDataHazards = new HashMap<>();
            for (HazardData hd : substanceDataHazards)  {
                mappedDataHazards.put(hd.getName(), hd);
            }

            Set<SubstanceHazard> substanceHazardSetDB = substanceDB.getSubstanceHazards();
            List<String> substanceHazardSetNamesDB = substanceHazardSetDB.stream()
                    .map(SubstanceHazard::getHazard)
                    .map(Hazard::getName)
                    .toList();

            for (HazardData hd : substanceDataHazards) {
                if (substanceHazardSetNamesDB.contains(hd.getName())) {
                    hd.setOperation(DatabaseOperation.NO_CHANGE);
                }
            }

            for (Hazard hazardDB : substanceHazardSetDB.stream().map(SubstanceHazard::getHazard).toList()) {
                if (!mappedDataHazards.containsKey(hazardDB.getName())) {
                    HazardData hd = new HazardData(hazardDB.getName(), hazardDB.getCode());
                    hd.setOperation(DatabaseOperation.REMOVE);
                    hd.setDatabaseRecordId(hazardDB.getId());
                    substanceData.getHazards().add(hd);
                }
            }
            mappedData.remove(substanceDB.getIndex());
        }
        mappedData.values().forEach(el -> el.setOperation(DatabaseOperation.ADD));
        hazardService.crossCheckHazards(mappedData);
    }

    public Sheet openFile(String filePath) throws FileNotFoundException {
        try (FileInputStream file = new FileInputStream(filePath)) {
            Workbook workbook = new XSSFWorkbook(file);
            return workbook.getSheetAt(0);
        } catch (IOException ex) {
            logger.error(String.format("File %s not found ", filePath), ex);
            throw new FileNotFoundException();
        }
    }

    public DocumentStatus checkStructure(String filePath) throws FileNotFoundException {
        return checkStructure(openFile(filePath));
    }

    public DocumentStatus checkStructure(Sheet sheet) {
        for (XLSXCellCoord coord : requiredCells) {
            try {
                Cell cell = sheet.getRow(coord.getRowNum()).getCell(coord.getColNum());
                String cellText = cell.getStringCellValue();
                if (!cellText.trim().equals(coord.getText())) {
                    return DocumentStatus.INVALID;
                }
            } catch (NullPointerException ex) {
                return DocumentStatus.INVALID;
            }
        }
        return DocumentStatus.VALID;
    }

    public String generateChangeset(String filePath) throws IOException {
        Sheet sheet = openFile(filePath);
        if (checkStructure(sheet) == DocumentStatus.INVALID)
            return null;

        List<SubstanceData> data = new ArrayList<>();
        List<String> indexes = new ArrayList<>();
        HashMap<String, SubstanceData> mappedData = new HashMap<>();
        int count = 0;
        for (int rowNum = 6; rowNum < sheet.getLastRowNum(); rowNum++) {
            Row row = sheet.getRow(rowNum);
            String index = row.getCell(COLUMNS.get("index")).getStringCellValue();
            if (index == null || index.isBlank())
                break;

            SubstanceData rowData = new SubstanceData();
            rowData.setIndex(index);
            rowData.setName(row.getCell(COLUMNS.get("substance")).getStringCellValue());
            rowData.setEcNo(row.getCell(COLUMNS.get("ec")).getStringCellValue());
            rowData.setCasNo(row.getCell(COLUMNS.get("cas")).getStringCellValue());
            try {
                rowData.setHazards(
                        matchHazardsWithCodes(
                                row.getCell(COLUMNS.get("hazards")),
                                row.getCell(COLUMNS.get("codes"))));
            } catch (Exception ex) {
                continue;
            }

            data.add(rowData);
            indexes.add(index);
            mappedData.put(index, rowData);
            count++;
            if (count == BATCH_SIZE) {
                filterChangesWithDatabaseEntities(data, mappedData, indexes);
                indexes = new ArrayList<>();
                mappedData = new HashMap<>();
                count = 0;
            }
        }
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.writeValue(Paths.get("/tmp/dane.json").toFile(), data);
        return "/tmp/dane.json";
    }
}
