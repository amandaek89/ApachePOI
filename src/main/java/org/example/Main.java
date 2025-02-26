package org.example;

import java.io.IOException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws IOException {
        Scanner scanner = new Scanner(System.in);
        ExcelService excelService = new ExcelService();

        while (true) {
            System.out.println("\n **Excel Hanterare** ");
            System.out.println("1. Skapa en ny Excel-fil");
            System.out.println("2. Läs textfärger från Excel");
            System.out.println("3. Läs textfärger och filtrera bort celler med GRÅ");
            System.out.println("4. Avsluta");
            System.out.print("Välj ett alternativ (1-4): ");

            int choice = scanner.nextInt();
            scanner.nextLine(); // Rensa radbrytning

            switch (choice) {
                case 1:
                    excelService.createExcelFile();
                    break;
                case 2:
                    excelService.readTextColorsFromFile(false);
                    break;
                case 3:
                    excelService.readTextColorsFromFile(true);
                    break;
                case 4:
                    System.out.println("Avslutar...");
                    scanner.close();
                    return;
                default:
                    System.out.println("Ogiltigt val, försök igen.");
            }
        }
    }
}
