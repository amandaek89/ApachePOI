package org.example;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.*;

import java.io.*;

public class ExcelService {
    private static final String FILE_NAME = "textcolor.xlsx";

    public void createExcelFile() throws IOException {
        try (XSSFWorkbook wb = new XSSFWorkbook()) {
            Sheet sheet = wb.createSheet("TextColor Sheet");

            // Skapa rader med olika typer av data
            ExcelHelper.createRowWithTextColor(wb, sheet, 0, "Amanda", "Gray", 1, IndexedColors.GREY_80_PERCENT);
            ExcelHelper.createRowWithTextColor(wb, sheet, 1, "Björn", "Light Gray", 2, IndexedColors.GREY_50_PERCENT);
            ExcelHelper.createRowWithTextColor(wb, sheet, 2, "Cecilia", "Very Light Gray", 3, IndexedColors.GREY_40_PERCENT);
            ExcelHelper.createRowWithTextColor(wb, sheet, 3, "David", "Red", 4, IndexedColors.RED);
            ExcelHelper.createRowWithTextColor(wb, sheet, 4, "Emma", "Blue", 5, IndexedColors.BLUE);
            ExcelHelper.createRowWithTextColor(wb, sheet, 5, "Filip", "Green", 6, IndexedColors.GREEN);
            ExcelHelper.createRowWithTextColor(wb, sheet, 6, "Gabriella", "Yellow", 7, IndexedColors.YELLOW);
            ExcelHelper.createRowWithTextColor(wb, sheet, 7, "Hugo", "Orange", 8, IndexedColors.ORANGE);
            ExcelHelper.createRowWithTextColor(wb, sheet, 8, "Isabella", "Black", 9, IndexedColors.BLACK);

            try (FileOutputStream fileOut = new FileOutputStream(FILE_NAME)) {
                wb.write(fileOut);
                System.out.println("Fil '" + FILE_NAME + "' skapad med exempeldata!");
            }
        }
    }

    public void readTextColorsFromFile(boolean filterGray) throws IOException {
        File file = new File(FILE_NAME);
        if (!file.exists()) {
            System.out.println("Filen '" + FILE_NAME + "' hittades inte. Skapa filen först!");
            return;
        }

        try (FileInputStream fis = new FileInputStream(FILE_NAME);
             XSSFWorkbook wb = new XSSFWorkbook(fis)) {
            Sheet sheet = wb.getSheetAt(0);

            System.out.println("\n **Innehåll i Excel-filen** ");

            for (Row row : sheet) {
                Cell cell = row.getCell(0); // Läs första cellen i raden för att identifiera färgen
                if (cell != null) {
                    XSSFCellStyle cellStyle = (XSSFCellStyle) cell.getCellStyle();
                    XSSFFont font = cellStyle.getFont();
                    short colorIndex = font.getColor();

                    // Hämta färgnamn
                    String colorName = ExcelHelper.getColorName(colorIndex);

                    // Filtrera bort grå rader om alternativet är valt
                    if (filterGray && ExcelHelper.isGrayColor(colorIndex)) {
                        System.out.println("Skipping gray row: " + row.getRowNum());
                        continue;
                    }

                    // Läs in och skriv ut värden från alla tre kolumner
                    String name = ExcelHelper.getCellValue(row.getCell(0));
                    String color = ExcelHelper.getCellValue(row.getCell(1));
                    String number = ExcelHelper.getCellValue(row.getCell(2));

                    System.out.println("Rad [" + row.getRowNum() + "]: " + name + " | " + color + " | " + number + " -> Färg: " + colorName);
                }
            }
        }
    }
}
