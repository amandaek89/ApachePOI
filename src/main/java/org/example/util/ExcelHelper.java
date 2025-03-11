package org.example.util;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.*;

/**
 * Hjälpklass som innehåller metoder för att arbeta med Excel-filer (XSSF-format) med Apache POI.
 * Klassen tillhandahåller funktioner för att skapa celler med specifika textfärger, läsa cellvärden,
 * och kontrollera färgindex i Excel-dokument.
 */
public class ExcelHelper {

    /**
     * Skapar en rad i ett Excel-ark där alla celler har samma textfärg men olika typer av data.
     *
     * @param wb Arbetshäftet (Workbook) där rad och celler kommer att skapas.
     * @param sheet Bladet (Sheet) i arbetsboken där raden ska läggas till.
     * @param rowIndex Indexet för raden som ska skapas.
     * @param name Namnet (sträng) som ska sättas i den första cellen.
     * @param color En text som representerar färgen som ska sättas i den andra cellen.
     * @param number Ett numeriskt värde som ska sättas i den tredje cellen.
     * @param textColor Den textfärg som ska tillämpas på alla celler i raden.
     */
    public static void createRowWithTextColor(XSSFWorkbook wb, Sheet sheet, int rowIndex, String name, String color, int number, IndexedColors textColor) {
        Row row = sheet.createRow(rowIndex);

        XSSFFont font = wb.createFont();
        font.setColor(textColor.getIndex());

        CellStyle style = wb.createCellStyle();
        style.setFont(font);

        // Skapa tre celler med olika datatyper men samma textfärg
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

    /**
     * Returnerar färgnamnet för ett givet färgindex.
     *
     * @param colorIndex Färgindexet (kort) för den färg som ska identifieras.
     * @return Namnet på färgen som motsvarar indexet, eller "Unknown Color" om färgen inte finns.
     */
    public static String getColorName(short colorIndex) {
        for (IndexedColors indexedColor : IndexedColors.values()) {
            if (indexedColor.getIndex() == colorIndex) {
                return indexedColor.name();
            }
        }
        return "Unknown Color (" + colorIndex + ")";
    }

    /**
     * Kontrollerar om en given färg är en nyans av grått.
     *
     * @param colorIndex Färgindexet (kort) för den färg som ska kontrolleras.
     * @return true om färgen är en nyans av grått, annars false.
     */
    public static boolean isGrayColor(short colorIndex) {
        return colorIndex == IndexedColors.GREY_80_PERCENT.getIndex() ||
                colorIndex == IndexedColors.GREY_50_PERCENT.getIndex() ||
                colorIndex == IndexedColors.GREY_40_PERCENT.getIndex() ||
                colorIndex == IndexedColors.GREY_25_PERCENT.getIndex();
    }

    /**
     * Läser värdet från en cell och hanterar olika datatyper som strängar, nummer och booleans.
     *
     * @param cell Cellen från vilken värdet ska hämtas.
     * @return Cellens värde som en sträng, eller en beskrivning av okänd typ om cellens typ inte stöds.
     */
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


