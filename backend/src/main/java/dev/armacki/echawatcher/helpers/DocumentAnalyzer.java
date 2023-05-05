package dev.armacki.echawatcher.helpers;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Component
public class DocumentAnalyzer {

    private static final Logger logger = LoggerFactory.getLogger(DocumentAnalyzer.class);
    private static final List<XLSXCellCoord> requiredCells = new ArrayList<>(Arrays.asList(
            new XLSXCellCoord(4, 0, "Index No"),
            new XLSXCellCoord(4, 1, "International Chemical Identification"),
            new XLSXCellCoord(4, 2, "EC No"),
            new XLSXCellCoord(4, 3, "CAS No"),
            new XLSXCellCoord(5, 4, "Hazard Class and Category Code(s)"),
            new XLSXCellCoord(5, 5, "Hazard Statement Code(s)")
    ));

    public enum DocumentStatus {
        VALID,
        INVALID
    }

    public static DocumentStatus checkStructure(String filePath) throws FileNotFoundException {
        try (FileInputStream file = new FileInputStream(filePath)) {
            Workbook workbook = new XSSFWorkbook(file);
            Sheet sheet = workbook.getSheetAt(0);
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
        } catch (IOException ex) {
            logger.error(String.format("File %s not found ", filePath), ex);
            throw new FileNotFoundException();
        }
    }
}
