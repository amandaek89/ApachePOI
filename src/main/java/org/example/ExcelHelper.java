package org.example;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.*;

public class ExcelHelper {
    // Skapar en rad med textfärg
    public static void createRowWithTextColor(XSSFWorkbook wb, Sheet sheet, int rowIndex, String text, IndexedColors textColor) {
        Row row = sheet.createRow(rowIndex);

        XSSFFont font = wb.createFont();
        font.setColor(textColor.getIndex());

        CellStyle style = wb.createCellStyle();
        style.setFont(font);

        Cell cell = row.createCell(0);
        cell.setCellValue(text);
        cell.setCellStyle(style);
    }

    // Mappning av färgindex till färgnamn
    public static String getColorName(short colorIndex) {
        for (IndexedColors indexedColor : IndexedColors.values()) {
            if (indexedColor.getIndex() == colorIndex) {
                return indexedColor.name();
            }
        }
        return "Unknown Color (" + colorIndex + ")";
    }

    // Kollar om färgen är en grå nyans
    public static boolean isGrayColor(short colorIndex) {
        return colorIndex == IndexedColors.GREY_50_PERCENT.getIndex() ||
                colorIndex == IndexedColors.GREY_40_PERCENT.getIndex() ||
                colorIndex == IndexedColors.GREY_25_PERCENT.getIndex();
    }
}

