package org.example.service;

import org.example.model.TermImport;
import org.example.repository.TermImportRepository;
import org.example.util.ExcelReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.io.IOException;
import java.util.List;

/**
 * Serviceklass för att hantera import och hantering av TermImport-objekt.
 * Tillhandahåller metoder för att importera termer från Excel-filer och interagera med TermImport-repositoryt.
 */
@Service
public class TermImportService {

    private final TermImportRepository termImportRepository;
    private final ExcelReader excelReader;

    /**
     * Konstruktor för att initialisera TermImportService med dess beroenden.
     *
     * @param termImportRepository repositoryt som används för att hantera TermImport-objekt.
     * @param excelReader utility som används för att läsa Excel-filer.
     */
    @Autowired
    public TermImportService(TermImportRepository termImportRepository, ExcelReader excelReader) {
        this.termImportRepository = termImportRepository;
        this.excelReader = excelReader;
    }

    /**
     * Importerar termer från en Excel-fil och sparar dem i TermImport-repositoryt.
     * Termerna läses från filen och sparas sedan i databasen.
     *
     * @throws IOException om det uppstår ett fel vid läsning av Excel-filen.
     */
    public void importTermImports() throws IOException {
        List<TermImport> importedTerms = excelReader.readTermsFromFile(TermImport.class);
        termImportRepository.saveAll(importedTerms);
        System.out.println("Sparade " + importedTerms.size() + " importerade termer i databasen!");
    }

    /**
     * Hämtar ett TermImport-objekt baserat på dess typ och kod.
     *
     * @param type den typ av term att söka efter.
     * @param code den kod för termen att söka efter.
     * @return TermImport-objektet om det hittas, annars null.
     */
    public TermImport getTermImportByTypeAndCode(String type, String code) {
        return termImportRepository.findByTypeAndCode(type.trim(), code.trim()).orElse(null);
    }

    /**
     * Hämtar alla TermImport-objekt från repositoryt.
     *
     * @return en lista med alla TermImport-objekt.
     */
    public List<TermImport> getAllTermImports() {
        return termImportRepository.findAll();
    }

    /**
     * Skriver ut alla TermImport-objekt i databasen till konsolen.
     * Om inga objekt hittas, skrivs ett meddelande ut som indikerar att databasen är tom.
     */
    public void printAllTermImports() {
        List<TermImport> termImports = getAllTermImports();
        if (termImports.isEmpty()) {
            System.out.println("Inga TermImport-objekt finns i databasen.");
        } else {
            System.out.println("\n **TermImport-objekt i databasen:** ");
            for (TermImport termImport : termImports) {
                System.out.println(termImport);
            }
        }
    }
}
