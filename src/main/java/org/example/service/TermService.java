package org.example.service;

import org.example.model.Term;
import org.example.repository.TermRepository;
import org.example.util.ExcelReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.io.IOException;
import java.util.List;

/**
 * Serviceklass för att hantera termer.
 * Tillhandahåller metoder för att importera termer från Excel-filer och interagera med Term-repositoryt.
 */
@Service
public class TermService {

    private final TermRepository termRepository;
    private final ExcelReader excelReader;

    /**
     * Konstruktor för att initialisera TermService med dess beroenden.
     *
     * @param termRepository repositoryt som används för att hantera Term-objekt.
     * @param excelReader utility som används för att läsa Excel-filer.
     */
    @Autowired
    public TermService(TermRepository termRepository, ExcelReader excelReader) {
        this.termRepository = termRepository;
        this.excelReader = excelReader;
    }

    /**
     * Importerar termer från en Excel-fil och sparar dem i Term-repositoryt.
     * Termerna läses från filen och sparas sedan i databasen.
     *
     * @throws IOException om det uppstår ett fel vid läsning av Excel-filen.
     */
    public void importTerms() throws IOException {
        // Läs termer från Excel-fil
        List<Term> terms = excelReader.readTermsFromFile(Term.class);

        if (terms.isEmpty()) {
            System.out.println("Inga termer att importera.");
        } else {
            // Spara termer i databasen
            termRepository.saveAll(terms);
            System.out.println("Sparade " + terms.size() + " termer i databasen!");
        }
    }

    /**
     * Hämtar ett Term-objekt baserat på dess typ och kod.
     *
     * @param type den typ av term att söka efter.
     * @param code den kod för termen att söka efter.
     * @return Term-objektet om det hittas, annars null.
     */
    public Term getTermByTypeAndCode(String type, String code) {
        return termRepository.findByTypeAndCode(type.trim(), code.trim()).orElse(null);
    }

    /**
     * Hämtar alla Term-objekt från repositoryt.
     *
     * @return en lista med alla Term-objekt.
     */
    public List<Term> getAllTerms() {
        return termRepository.findAll();
    }
}
