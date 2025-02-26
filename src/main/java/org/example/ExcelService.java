package org.example;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.*;

import java.io.*;

public class ExcelService {
    private static final String FILE_NAME = "textcolor.xlsx";

    public void createExcelFile() throws IOException {
        try (XSSFWorkbook wb = new XSSFWorkbook()) {
            Sheet sheet = wb.createSheet("TextColor Sheet");

            // Skapa exempeldata med olika textfärger
            ExcelHelper.createRowWithTextColor(wb, sheet, 0, "Gray Text", IndexedColors.GREY_50_PERCENT);
            ExcelHelper.createRowWithTextColor(wb, sheet, 1, "Red Text", IndexedColors.RED);
            ExcelHelper.createRowWithTextColor(wb, sheet, 2, "Blue Text", IndexedColors.BLUE);
            ExcelHelper.createRowWithTextColor(wb, sheet, 3, "Green Text", IndexedColors.GREEN);
            ExcelHelper.createRowWithTextColor(wb, sheet, 4, "Yellow Text", IndexedColors.YELLOW);
            ExcelHelper.createRowWithTextColor(wb, sheet, 5, "Orange Text", IndexedColors.ORANGE);
            ExcelHelper.createRowWithTextColor(wb, sheet, 6, "Black Text", IndexedColors.BLACK);
            ExcelHelper.createRowWithTextColor(wb, sheet, 7, "Pink Text", IndexedColors.PINK);
            ExcelHelper.createRowWithTextColor(wb, sheet, 8, "Gray Text Again", IndexedColors.GREY_40_PERCENT);

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

            System.out.println("\n **Textfärger i Excel-filen** ");

            for (Row row : sheet) {
                Cell cell = row.getCell(0);
                if (cell != null) {
                    XSSFCellStyle cellStyle = (XSSFCellStyle) cell.getCellStyle();
                    XSSFFont font = cellStyle.getFont();
                    short colorIndex = font.getColor();

                    // Hämta färgnamn
                    String colorName = ExcelHelper.getColorName(colorIndex);

                    // Filtrera bort grå text om alternativet är valt
                    if (filterGray && ExcelHelper.isGrayColor(colorIndex)) {
                        System.out.println("Skipping gray text: " + cell.getStringCellValue());
                        continue;
                    }

                    System.out.println("Cell [" + row.getRowNum() + "]: " + cell.getStringCellValue() + " -> Färg: " + colorName);
                }
            }
        }
    }
}
