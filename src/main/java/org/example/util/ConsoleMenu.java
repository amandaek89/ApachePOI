package org.example.util;

import org.example.model.Term;
import org.example.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;
import java.util.Scanner;

/**
 * Konsolmeny som hanterar användarinteraktion för att utföra olika operationer
 * relaterade till Excel-filer, termer och deras status.
 * Klienten får välja mellan olika alternativ som att skapa Excel-filer,
 * läsa data, importera termer till databasen, och uppdatera termer baserat på status.
 */
@Component
public class ConsoleMenu implements CommandLineRunner {

    private final ExcelService excelService;
    private final TermService termService;
    private final TermUpdateService updateService;
    private final TermImportService termImportService;
    private final TermComparisonService termComparisonService;

    /**
     * Konstruktor som initialiserar alla beroenden som behövs för menyn.
     *
     * @param excelService       tjänst för att hantera Excel-relaterade operationer.
     * @param termService        tjänst för att hantera termer.
     * @param updateService      tjänst för att uppdatera termer.
     * @param termImportService tjänst för att importera termer.
     * @param termComparisonService tjänst för att jämföra termer och sätta deras status.
     */
    @Autowired
    public ConsoleMenu(ExcelService excelService, TermService termService, TermUpdateService updateService,
                       TermImportService termImportService, TermComparisonService termComparisonService) {
        this.excelService = excelService;
        this.termService = termService;
        this.updateService = updateService;
        this.termImportService = termImportService;
        this.termComparisonService = termComparisonService;
    }

    /**
     * Metod som körs när programmet startar. Denna metod presenterar ett menyalternativ
     * för användaren och låter användaren välja en åtgärd via konsolen.
     *
     * @param args eventuella argument som skickas vid start (inte använda här).
     * @throws IOException om något går fel vid inläsning av Excel-filer.
     */
    @Override
    public void run(String... args) throws IOException {
        Scanner scanner = new Scanner(System.in);

        while (true) {
            // Skriver ut menyalternativ
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
            System.out.println("15. Avsluta");
            System.out.print("Välj ett alternativ (1-15): ");

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
                    System.out.println("Avslutar...");
                    return;
                default:
                    System.out.println("Ogiltigt val, försök igen.");
            }
        }
    }
}
