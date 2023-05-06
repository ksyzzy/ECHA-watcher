package dev.armacki.echawatcher.helpers;

import dev.armacki.echawatcher.entities.Substance;
import lombok.Getter;
import lombok.Setter;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;

@Component
public class DocumentAnalyzer {

    @Getter @Setter
    public static class RowData {
        private String index;
        private String name;
        private String ecNo;
        private String casNo;
        private Map<String, String> hazards = new HashMap<>();
    }

    private static final Logger logger = LoggerFactory.getLogger(DocumentAnalyzer.class);
    private static final int BATCH_SIZE = 200;
    private static final List<XLSXCellCoord> requiredCells = new ArrayList<>(Arrays.asList(
            new XLSXCellCoord(4, 0, "Index No"),
            new XLSXCellCoord(4, 1, "International Chemical Identification"),
            new XLSXCellCoord(4, 2, "EC No"),
            new XLSXCellCoord(4, 3, "CAS No"),
            new XLSXCellCoord(5, 4, "Hazard Class and Category Code(s)"),
            new XLSXCellCoord(5, 5, "Hazard Statement Code(s)")
    ));

    private static final HashMap<String, Integer> COLUMNS = new HashMap<>(Map.of(
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

    private Substance getDataFromCell(Cell cell) {
        return new Substance();
    }

    public static Sheet openFile(String filePath) throws FileNotFoundException {
        try (FileInputStream file = new FileInputStream(filePath)) {
            Workbook workbook = new XSSFWorkbook(file);
            return workbook.getSheetAt(0);
        } catch (IOException ex) {
            logger.error(String.format("File %s not found ", filePath), ex);
            throw new FileNotFoundException();
        }
    }

    public static DocumentStatus checkStructure(String filePath) throws FileNotFoundException {
        return checkStructure(openFile(filePath));
    }

    public static DocumentStatus checkStructure(Sheet sheet) {
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

    public static String generateChangeset(String filePath) throws FileNotFoundException {
        Sheet sheet = openFile(filePath);
        if (checkStructure(sheet) == DocumentStatus.INVALID)
            return null;

        Row row = sheet.getRow(6);
        List<RowData> data = new ArrayList<>();
        int count = 0;
        for (int rowNum = 6; rowNum < sheet.getLastRowNum(); rowNum++) {
            String index = row.getCell(COLUMNS.get("index")).getStringCellValue();
            if (index == null || index.isBlank())
                break;

            RowData rowData = new RowData();
            rowData.setIndex(index);
            rowData.setName(row.getCell(COLUMNS.get("substance")).getStringCellValue());
            rowData.setEcNo(row.getCell(COLUMNS.get("ec")).getStringCellValue());
            rowData.setCasNo(row.getCell(COLUMNS.get("cas")).getStringCellValue());
            count++;
            if (count == BATCH_SIZE) {
                // apply changes in batch
            }
        }
    }
}
