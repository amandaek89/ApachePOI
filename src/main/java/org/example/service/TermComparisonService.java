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

@Service
public class TermComparisonService {

    private final TermRepository termRepository;

    private final TermImportRepository termImportRepository;

    private final TermService termService;

    private final TermImportService termImportService;

    private final TermMapper termMapper;


    public TermComparisonService(TermRepository termRepository, TermImportRepository termImportRepository, TermService termService, TermImportService termImportService, TermMapper termMapper) {
        this.termRepository = termRepository;
        this.termImportRepository = termImportRepository;
        this.termService = termService;
        this.termImportService = termImportService;
        this.termMapper = termMapper;
    }

    // Metod för att jämföra och sätta status på termer
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


    // Metod för att hämta alla termer med status NEW
    public List<Term> getNewTerms() {
        return termRepository.findAll().stream()
                .filter(term -> term.getStatus() == TermStatus.NEW)
                .collect(Collectors.toList());
    }

    // Metod för att hämta alla termer med status UPDATED
    public List<Term> getUpdatedTerms() {
        return termRepository.findAll().stream()
                .filter(term -> term.getStatus() == TermStatus.UPDATED)
                .collect(Collectors.toList());
    }

    // Metod för att hämta alla termer med status DELETED
    public List<Term> getDeletedTerms() {
        return termRepository.findAll().stream()
                .filter(term -> term.getStatus() == TermStatus.DELETED)
                .collect(Collectors.toList());
    }

    // Metod för att hämta alla termer med status UNCHANGED
    public List<Term> getUnchangedTerms() {
        return termRepository.findAll().stream()
                .filter(term -> term.getStatus() == TermStatus.UNCHANGED)
                .collect(Collectors.toList());
    }

    public void printNewTerms() {
        List<Term> newTerms = getNewTerms();  // Hämta alla termer med status NEW
        if (newTerms.isEmpty()) {
            System.out.println("Inga termer med status NEW.");
        } else {
            System.out.println("Termer med status NEW:");
            newTerms.forEach(term -> System.out.println(term));
        }
    }

    public void printUpdatedTerms() {
        List<Term> updatedTerms = getUpdatedTerms();  // Hämta alla termer med status UPDATED
        if (updatedTerms.isEmpty()) {
            System.out.println("Inga termer med status UPDATED.");
        } else {
            System.out.println("Termer med status UPDATED:");
            updatedTerms.forEach(term -> System.out.println(term));
        }
    }

    public void printDeletedTerms() {
        List<Term> deletedTerms = getDeletedTerms();  // Hämta alla termer med status DELETED
        if (deletedTerms.isEmpty()) {
            System.out.println("Inga termer med status DELETED.");
        } else {
            System.out.println("Termer med status DELETED:");
            deletedTerms.forEach(term -> System.out.println(term));
        }
    }

    public void printUnchangedTerms() {
        List<Term> unchangedTerms = getUnchangedTerms();  // Hämta alla termer med status UNCHANGED
        if (unchangedTerms.isEmpty()) {
            System.out.println("Inga termer med status UNCHANGED.");
        } else {
            System.out.println("Termer med status UNCHANGED:");
            unchangedTerms.forEach(term -> System.out.println(term));
        }
    }

    // Metod för att visa alla termer med deras respektive status
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

    // Metod för att uppdatera en befintlig term i Term-tabellen om den är UPDATED
    private void updateExistingTerm(Term termToUpdate, TermImport termImport) {
        // Uppdatera termen med värden från TermImport
        termToUpdate.setText(termImport.getText());
        termToUpdate.setEgNumber(termImport.getEgNumber());
        termToUpdate.setVehicleType(termImport.getVehicleType());

        // Sätt statusen till UPDATED om det behövs
        termToUpdate.setStatus(TermStatus.UPDATED);

        // Spara den uppdaterade termen i Term-tabellen
        termRepository.save(termToUpdate);
        System.out.println("Termen med type: " + termToUpdate.getType() + " och code: " + termToUpdate.getCode() + " har uppdaterats i Term-tabellen.");
    }

    private void createNewTerm(TermImport termImport) {
        // Skapa en ny Term från TermImport
        Term newTerm = new Term();
        newTerm.setCode(termImport.getCode());
        newTerm.setType(termImport.getType());
        newTerm.setText(termImport.getText());
        newTerm.setEgNumber(termImport.getEgNumber());
        newTerm.setVehicleType(termImport.getVehicleType());
        newTerm.setStatus(TermStatus.NEW);  // sätt statusen till NEW, kan vara användbart för senare referens

        // Spara den nya termen i Term-tabellen
        termRepository.save(newTerm);
        System.out.println("Ny term med type: " + termImport.getType() + " och code: " + termImport.getCode() + " har skapats i Term-tabellen.");
    }

    private void deleteTerm(Term termToDelete) {
        // Kontrollera om termen finns i Term-tabellen
        if (termToDelete != null) {
            termRepository.delete(termToDelete);  // Ta bort termen från Term-tabellen
            System.out.println("Termen med type: " + termToDelete.getType() + " och code: " + termToDelete.getCode() + " har raderats från Term-tabellen.");
        } else {
            System.out.println("Ingen term hittades att radera.");
        }
    }


}