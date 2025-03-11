package org.example.util;

import org.example.model.Term;
import org.example.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;
import java.util.Scanner;

@Component
public class ConsoleMenu implements CommandLineRunner {

    private final ExcelService excelService;
    private final TermService termService;
    private final TermUpdateService updateService;
    private final TermImportService termImportService;
    private final TermComparisonService termComparisonService;

    @Autowired
    public ConsoleMenu(ExcelService excelService, TermService termService, TermUpdateService updateService,
                       TermImportService termImportService, TermComparisonService termComparisonService) {
        this.excelService = excelService;
        this.termService = termService;
        this.updateService = updateService;
        this.termImportService = termImportService;
        this.termComparisonService = termComparisonService;
    }

    @Override
    public void run(String... args) throws IOException {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("\n **Excel Hanterare** ");
            System.out.println("1. Skapa en ny Excel-fil");
            System.out.println("2. Läs textfärger från Excel");
            System.out.println("3. Läs textfärger och filtrera bort celler med GRÅ");
            System.out.println("4. Importera data från Excel till databasen");
            System.out.println("5. Visa alla termer från Term-tabellen");
            System.out.println("6. Importera termer till TermImport-tabellen");
            System.out.println("7. Visa TermImport-tabellen");
            System.out.println("8. Utför jämförelse och sätt status");
            System.out.println("9. Visa termer med status NEW");
            System.out.println("10. Visa termer med status UPDATED");
            System.out.println("11. Visa termer med status DELETED");
            System.out.println("12. Visa termer med status UNCHANGED");
            System.out.println("13. Visa alla termer med deras status");
            System.out.println("14. Uppdatera status på en term");
            System.out.println("15. Sök efter en term i både Term och TermImport-tabellerna");
            System.out.println("16. Avsluta");
            System.out.print("Välj ett alternativ (1-16): ");

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
                    System.out.println("Importerar data från Excel till databasen...");
                    termService.importTerms();
                    System.out.println("Import färdig!");
                    break;
                case 5:
                    System.out.println("Hämtar alla termer från Term-tabellen...");
                    List<Term> terms = termService.getAllTerms();
                    terms.forEach(System.out::println);
                    break;
                case 6:
                    System.out.println("Importerar termer till TermImport-tabellen...");
                    termImportService.importTermImports();
                    System.out.println("Import färdig!");
                    break;
                case 7:
                    System.out.println("Visar termer i TermImport-tabellen...");
                    termImportService.printAllTermImports();
                    break;
                case 8:
                    System.out.println("Utför jämförelse och sätter status...");
                    termComparisonService.compareAndSetStatus();
                    break;
                case 9:
                    System.out.println("Visar termer med status NEW...");
                    termComparisonService.printNewTerms();
                    break;
                case 10:
                    System.out.println("Visar termer med status UPDATED...");
                    termComparisonService.printUpdatedTerms();
                    break;
                case 11:
                    System.out.println("Visar termer med status DELETED...");
                    termComparisonService.printDeletedTerms();
                    break;
                case 12:
                    System.out.println("Visar termer med status UNCHANGED...");
                    termComparisonService.printUnchangedTerms();
                    break;
                case 13:
                    System.out.println("Visar alla termer med deras status...");
                    termComparisonService.showAllTermsWithStatus();
                    break;
                case 14:
                    System.out.println("Ange type och code för termen du vill uppdatera:");
                    System.out.print("Type: ");
                    String typeToUpdate = scanner.nextLine();
                    System.out.print("Code: ");
                    String codeToUpdate = scanner.nextLine();

                    updateService.saveOrUpdateTerm(typeToUpdate, codeToUpdate);
                    break;
                case 15:
                    System.out.println("Ange type och code för att söka efter termen:");
                    System.out.print("Type: ");
                    String searchType = scanner.nextLine().trim();
                    System.out.print("Code: ");
                    String searchCode = scanner.nextLine().trim();

                    // Hämta termen från båda tabellerna (Optional används för att hantera null-säkerhet)
                    updateService.getTermFromBothTables(searchType, searchCode)
                            .ifPresentOrElse(
                                    term -> System.out.println("Hittade termen: " + term),
                                    () -> System.out.println("Ingen term hittades med type: " + searchType + " och code: " + searchCode)
                            );
                    break;
                case 16:
                    System.out.println("Avslutar...");
                    return;
                default:
                    System.out.println("Ogiltigt val, försök igen.");
            }
        }
    }
}
