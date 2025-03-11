package org.example.util;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.example.model.Term;
import org.example.model.TermImport;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Component
public class ExcelReader {

    public <T> List<T> readTermsFromFile(Class<T> termClass) throws IOException {
        String filePath = "C:\\Users\\00324322\\OneDrive - Nexer AB\\Dokument\\besiktningsorgan.-termkatalog-fordonsbesiktning_tsid-27-318 (12)-Dekra.xlsx";
        File file = new File(filePath);
        List<T> terms = new ArrayList<>();

        if (!file.exists()) {
            System.out.println("Filen hittades inte: " + filePath);
            return terms;
        }

        try (FileInputStream fis = new FileInputStream(file);
             XSSFWorkbook wb = new XSSFWorkbook(fis)) {

            // Hämta arket med namnet "BesiktningsProgram"
            Sheet sheet = wb.getSheet("BesiktningsProgram");
            if (sheet == null) {
                System.out.println("Arket 'BesiktningsProgram' hittades inte i filen.");
                return terms;
            }

            System.out.println("\n **Läser data från Excel (BesiktningsProgram) och sparar som Term-objekt** ");

            boolean isHeaderSkipped = false;

            for (Row row : sheet) {
                if (row == null) continue;

                Cell firstCell = row.getCell(0);
                if (firstCell == null || firstCell.getCellType() == CellType.BLANK) continue;

                // Hoppa över header-raden
                if (!isHeaderSkipped) {
                    isHeaderSkipped = true;
                    continue;
                }

                // Kontrollera om textfärgen är grå (endast för TermImport)
                if (termClass.equals(TermImport.class)) {
                    CellStyle style = firstCell.getCellStyle();
                    if (style != null) {
                        XSSFFont font = ((XSSFCellStyle) style).getFont();
                        if (ExcelHelper.isGrayColor(font.getColor())) {
                            continue; // Hoppa över rader där texten är grå
                        }
                    }
                }

                // Skapa antingen Term eller TermImport beroende på vilken klass som efterfrågas
                if (termClass.equals(Term.class)) {
                    Term term = new Term();
                    term.setType("BesiktningsProgram");
                    term.setCode(ExcelHelper.getCellValue(row.getCell(0))); // Kolumn A → code
                    term.setText(ExcelHelper.getCellValue(row.getCell(1))); // Kolumn B → text
                    terms.add(termClass.cast(term));
                } else if (termClass.equals(TermImport.class)) {
                    TermImport termImport = new TermImport();
                    termImport.setType("BesiktningsProgram");
                    termImport.setCode(ExcelHelper.getCellValue(row.getCell(0))); // Kolumn A → code
                    termImport.setText(ExcelHelper.getCellValue(row.getCell(1))); // Kolumn B → text
                    terms.add(termClass.cast(termImport));
                }
            }
        }
        return terms;
    }
}
