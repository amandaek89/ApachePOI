package org.example.util;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.*;

public class ExcelHelper {

    // Skapar en rad där alla celler har samma textfärg men olika typer av data
    public static void createRowWithTextColor(XSSFWorkbook wb, Sheet sheet, int rowIndex, String name, String color, int number, IndexedColors textColor) {
        Row row = sheet.createRow(rowIndex);

        XSSFFont font = wb.createFont();
        font.setColor(textColor.getIndex());

        CellStyle style = wb.createCellStyle();
        style.setFont(font);

        // Skapa tre kolumner med olika datatyper men samma textfärg
        Cell cell1 = row.createCell(0);
        cell1.setCellValue(name);
        cell1.setCellStyle(style);

        Cell cell2 = row.createCell(1);
        cell2.setCellValue(color);
        cell2.setCellStyle(style);

        Cell cell3 = row.createCell(2);
        cell3.setCellValue(number);
        cell3.setCellStyle(style);
    }

    // Returnerar färgnamnet baserat på dess index
    public static String getColorName(short colorIndex) {
        for (IndexedColors indexedColor : IndexedColors.values()) {
            if (indexedColor.getIndex() == colorIndex) {
                return indexedColor.name();
            }
        }
        return "Unknown Color (" + colorIndex + ")";
    }

    // Kontrollerar om färgen är en nyans av grått
    public static boolean isGrayColor(short colorIndex) {
        return colorIndex == IndexedColors.GREY_80_PERCENT.getIndex() ||
                colorIndex == IndexedColors.GREY_50_PERCENT.getIndex() ||
                colorIndex == IndexedColors.GREY_40_PERCENT.getIndex() ||
                colorIndex == IndexedColors.GREY_25_PERCENT.getIndex();
    }

    // Läser cellvärdet och hanterar olika datatyper (text, nummer, blandad text/nummer)
    public static String getCellValue(Cell cell) {
        if (cell == null) {
            return "";
        }
        switch (cell.getCellType()) {
            case STRING:
                return cell.getStringCellValue();
            case NUMERIC:
                return String.valueOf(cell.getNumericCellValue());
            case BOOLEAN:
                return String.valueOf(cell.getBooleanCellValue());
            default:
                return "Okänd typ";
        }
    }
}


