package org.example.service;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.*;
import org.example.util.ExcelHelper;
import org.springframework.stereotype.Service;

import java.io.*;

@Service
public class ExcelService {
    private static final String FILE_NAME = "textcolor.xlsx";

    public void createExcelFile() throws IOException {
        try (XSSFWorkbook wb = new XSSFWorkbook()) {
            Sheet sheet = wb.createSheet("TextColor Sheet");

            // Skapa rader med olika typer av data
            ExcelHelper.createRowWithTextColor(wb, sheet, 1, "Björn", "Light Gray", 2, IndexedColors.GREY_50_PERCENT);
            ExcelHelper.createRowWithTextColor(wb, sheet, 2, "Cecilia", "Very Light Gray", 3, IndexedColors.GREY_40_PERCENT);
            ExcelHelper.createRowWithTextColor(wb, sheet, 3, "David", "Red", 4, IndexedColors.RED);
            ExcelHelper.createRowWithTextColor(wb, sheet, 4, "Emma", "Blue", 5, IndexedColors.BLUE);
            ExcelHelper.createRowWithTextColor(wb, sheet, 5, "Filip", "Green", 6, IndexedColors.GREEN);
            ExcelHelper.createRowWithTextColor(wb, sheet, 6, "Gabriella", "Yellow", 7, IndexedColors.YELLOW);
            ExcelHelper.createRowWithTextColor(wb, sheet, 7, "Hugo", "Orange", 8, IndexedColors.ORANGE);
            ExcelHelper.createRowWithTextColor(wb, sheet, 8, "Isabella", "Black", 9, IndexedColors.BLACK);
            ExcelHelper.createRowWithTextColor(wb, sheet, 9, "Amanda", "Gray", 1, IndexedColors.GREY_80_PERCENT);

            try (FileOutputStream fileOut = new FileOutputStream(FILE_NAME)) {
                wb.write(fileOut);
                System.out.println("Fil '" + FILE_NAME + "' skapad med exempeldata!");
            }
        }
    }

    public void readTextColorsFromFile(boolean filterGray) throws IOException {
        String filePath = "C:\\Users\\00324322\\IdeaProjects\\poi-excel\\textcolor.xlsx";
        //String filePath = "C:\\Users\\00324322\\OneDrive - Nexer AB\\Dokument\\besiktningsorgan.-termkatalog-fordonsbesiktning_tsid-27-318 (12)-Dekra.xlsx";
        File file = new File(filePath);

        if (!file.exists()) {
            System.out.println("Filen hittades inte: " + filePath);
            return;
        }

        try (FileInputStream fis = new FileInputStream(file);
             XSSFWorkbook wb = new XSSFWorkbook(fis)) {

            //Sheet sheet = wb.getSheet("Besiktningsprogram");
            Sheet sheet = wb.getSheetAt(0);

            if (sheet == null) {
                System.out.println("Arket hittades inte i filen.");
                return;
            }

            System.out.println("\n **Innehåll i arket** ");

            int emptyRowCount = 0; // Räknare för tomma rader
            boolean firstRowChecked = false; // Kontrollflagga för att hoppa över första raden

            for (Row row : sheet) {

                if (row == null) {
                    emptyRowCount++;
                    if (emptyRowCount >= 2) break; // Avsluta om vi har 2 tomma rader i rad
                    continue;
                }

                Cell firstCell = row.getCell(0); // Läs första cellen i raden för att identifiera färgen
                if (firstCell == null || firstCell.getCellType() == CellType.BLANK) {
                    emptyRowCount++;
                    if (emptyRowCount >= 2) break; // Avsluta om vi har 2 tomma rader i rad
                    continue;
                }

                emptyRowCount = 0; // Återställ tomräknaren om en rad har data

                // Kolla om första raden verkar vara en header (inga siffror i tredje kolumnen)
                if (!firstRowChecked) {
                    firstRowChecked = true;
                    Cell thirdCell = row.getCell(2);
                    if (thirdCell != null && thirdCell.getCellType() == CellType.STRING) {
                        System.out.println("Hittade header, hoppar över den: " + row.getRowNum());
                        continue; // Hoppa över header-raden
                    }
                }

                XSSFCellStyle cellStyle = (XSSFCellStyle) firstCell.getCellStyle();
                XSSFFont font = cellStyle.getFont();
                short colorIndex = font.getColor();

                // Hämta färgnamn
                String colorName = ExcelHelper.getColorName(colorIndex);

                // Filtrera bort grå rader om alternativet är valt
                if (filterGray && ExcelHelper.isGrayColor(colorIndex)) {
                    System.out.println("Skipping gray row: " + (row.getRowNum() + 1)); // Excel börjar på rad 1
                    continue;
                }

                // Läs in och skriv ut värden från alla tre kolumner
                String name = ExcelHelper.getCellValue(row.getCell(0));
                String color = ExcelHelper.getCellValue(row.getCell(1));
                String number = ExcelHelper.getCellValue(row.getCell(2));

                // Kontrollera att namn kolumnen har värde innan utskrift
                if (name == null) continue;

                System.out.println("Rad [" + (row.getRowNum() + 1) + "]: " + name + " | " + color + " | " + number + " -> Färg: " + colorName);
            }
        }
    }
}
