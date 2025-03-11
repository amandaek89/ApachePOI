package org.example.service;
import org.example.mapper.TermMapper;
import org.example.model.Term;
import org.example.model.TermImport;
import org.example.model.TermStatus;
import org.example.repository.TermRepository;
import org.example.repository.TermImportRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Service för att jämföra och uppdatera termer mellan Term och TermImport-tabellerna.
 * Den här klassen hanterar jämförelse av termer och uppdatering av status (NEW, UPDATED, UNCHANGED, DELETED).
 */
@Service
public class TermComparisonService {

    private final TermRepository termRepository;
    private final TermImportRepository termImportRepository;
    private final TermService termService;
    private final TermImportService termImportService;
    private final TermMapper termMapper;

    /**
     * Konstruktor för att initiera TermComparisonService med alla beroenden.
     *
     * @param termRepository       Repository för att hantera Term-data.
     * @param termImportRepository Repository för att hantera TermImport-data.
     * @param termService          Service för att hantera logik relaterad till Term.
     * @param termImportService    Service för att hantera logik relaterad till TermImport.
     * @param termMapper           Mapper för att konvertera mellan Term och TermImport.
     */
    public TermComparisonService(TermRepository termRepository, TermImportRepository termImportRepository, TermService termService, TermImportService termImportService, TermMapper termMapper) {
        this.termRepository = termRepository;
        this.termImportRepository = termImportRepository;
        this.termService = termService;
        this.termImportService = termImportService;
        this.termMapper = termMapper;
    }

    /**
     * Jämför termer från Term-tabellen med TermImport-tabellen och uppdaterar status.
     * Sätter status på termer baserat på om de är nya, uppdaterade, oförändrade eller borttagna.
     */
    public void compareAndSetStatus() {
        List<Term> existingTerms = termRepository.findAll();  // Hämta termer från Term-tabellen
        List<TermImport> importedTerms = termImportRepository.findAll(); // Hämta termer från TermImport-tabellen

        // Skapa en map för snabb sökning av existerande termer baserat på code
        Map<String, Term> existingTermMap = existingTerms.stream()
                .collect(Collectors.toMap(Term::getCode, term -> term));

        for (TermImport importedTerm : importedTerms) {
            Term existingTerm = existingTermMap.get(importedTerm.getCode());

            if (existingTerm == null) {
                // Om termen inte finns i Term-tabellen, sätt status till NEW i TermImport
                importedTerm.setStatus(TermStatus.NEW);
            } else if (!existingTerm.getText().equals(importedTerm.getText())) {
                // Om texten inte matchar, sätt status till UPDATED i TermImport
                importedTerm.setStatus(TermStatus.UPDATED);
            } else {
                // Om termen är oförändrad, sätt status till UNCHANGED i både Term och TermImport
                importedTerm.setStatus(TermStatus.UNCHANGED);
                existingTerm.setStatus(TermStatus.UNCHANGED);  // Sätt status till UNCHANGED även i Term-tabellen
            }
            // Spara termen i TermImport-tabellen
            termImportRepository.save(importedTerm);

            // Spara den uppdaterade termen i Term-tabellen (om den ändras)
            termRepository.save(existingTerm);
        }

        // Hantera borttagna termer (termer som finns i Term men inte i TermImport)
        for (Term existingTerm : existingTerms) {
            boolean existsInImport = importedTerms.stream()
                    .anyMatch(imported -> imported.getCode().equals(existingTerm.getCode()));

            if (!existsInImport) {
                existingTerm.setStatus(TermStatus.DELETED);
                termRepository.save(existingTerm);  // Spara borttagen term i Term-tabellen
            }
        }

        System.out.println("Jämförelse och statusuppdatering färdig!");
    }

    /**
     * Hämtar alla termer med status NEW.
     *
     * @return en lista med termer med status NEW.
     */
    public List<Term> getNewTerms() {
        return termRepository.findAll().stream()
                .filter(term -> term.getStatus() == TermStatus.NEW)
                .collect(Collectors.toList());
    }

    /**
     * Hämtar alla termer med status UPDATED.
     *
     * @return en lista med termer med status UPDATED.
     */
    public List<Term> getUpdatedTerms() {
        return termRepository.findAll().stream()
                .filter(term -> term.getStatus() == TermStatus.UPDATED)
                .collect(Collectors.toList());
    }

    /**
     * Hämtar alla termer med status DELETED.
     *
     * @return en lista med termer med status DELETED.
     */
    public List<Term> getDeletedTerms() {
        return termRepository.findAll().stream()
                .filter(term -> term.getStatus() == TermStatus.DELETED)
                .collect(Collectors.toList());
    }

    /**
     * Hämtar alla termer med status UNCHANGED.
     *
     * @return en lista med termer med status UNCHANGED.
     */
    public List<Term> getUnchangedTerms() {
        return termRepository.findAll().stream()
                .filter(term -> term.getStatus() == TermStatus.UNCHANGED)
                .collect(Collectors.toList());
    }

    /**
     * Skriver ut alla termer med status NEW.
     */
    public void printNewTerms() {
        List<Term> newTerms = getNewTerms();  // Hämta alla termer med status NEW
        if (newTerms.isEmpty()) {
            System.out.println("Inga termer med status NEW.");
        } else {
            System.out.println("Termer med status NEW:");
            newTerms.forEach(term -> System.out.println(term));
        }
    }

    /**
     * Skriver ut alla termer med status UPDATED.
     */
    public void printUpdatedTerms() {
        List<Term> updatedTerms = getUpdatedTerms();  // Hämta alla termer med status UPDATED
        if (updatedTerms.isEmpty()) {
            System.out.println("Inga termer med status UPDATED.");
        } else {
            System.out.println("Termer med status UPDATED:");
            updatedTerms.forEach(term -> System.out.println(term));
        }
    }

    /**
     * Skriver ut alla termer med status DELETED.
     */
    public void printDeletedTerms() {
        List<Term> deletedTerms = getDeletedTerms();  // Hämta alla termer med status DELETED
        if (deletedTerms.isEmpty()) {
            System.out.println("Inga termer med status DELETED.");
        } else {
            System.out.println("Termer med status DELETED:");
            deletedTerms.forEach(term -> System.out.println(term));
        }
    }

    /**
     * Skriver ut alla termer med status UNCHANGED.
     */
    public void printUnchangedTerms() {
        List<Term> unchangedTerms = getUnchangedTerms();  // Hämta alla termer med status UNCHANGED
        if (unchangedTerms.isEmpty()) {
            System.out.println("Inga termer med status UNCHANGED.");
        } else {
            System.out.println("Termer med status UNCHANGED:");
            unchangedTerms.forEach(term -> System.out.println(term));
        }
    }

    /**
     * Visar alla termer med deras respektive status.
     */
    public void showAllTermsWithStatus() {
        List<Term> newTerms = getNewTerms();  // Hämta termer med status NEW
        List<Term> updatedTerms = getUpdatedTerms();  // Hämta termer med status UPDATED
        List<Term> deletedTerms = getDeletedTerms();  // Hämta termer med status DELETED
        List<Term> unchangedTerms = getUnchangedTerms();  // Hämta termer med status UNCHANGED

        // Skriv ut alla termer med deras status
        System.out.println("Termer med status NEW:");
        newTerms.forEach(term -> System.out.println(term));

        System.out.println("\nTermer med status UPDATED:");
        updatedTerms.forEach(term -> System.out.println(term));

        System.out.println("\nTermer med status DELETED:");
        deletedTerms.forEach(term -> System.out.println(term));

        System.out.println("\nTermer med status UNCHANGED:");
        unchangedTerms.forEach(term -> System.out.println(term));
    }
}