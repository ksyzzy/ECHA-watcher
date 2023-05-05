package dev.armacki.echawatcher.helpers;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class XLSXCellCoord {


    private int rowNum;
    private int colNum;
    private String text;

    /**
     * Creates EXCEL file's cell abstraction for XSLX files analysis
     * @param rowNum 0-based row number
     * @param colNum 0-based column number
     * @param text Expected text in specified cell
     */
    public XLSXCellCoord(int rowNum, int colNum, String text) {
        this.rowNum = rowNum;
        this.colNum = colNum;
        this.text = text;
    }
}
