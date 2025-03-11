package org.example.service;

import org.example.model.TermImport;
import org.example.repository.TermImportRepository;
import org.example.util.ExcelReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.io.IOException;
import java.util.List;

@Service
public class TermImportService {

    private final TermImportRepository termImportRepository;

    private final ExcelReader excelReader;

    @Autowired
    public TermImportService(TermImportRepository termImportRepository, ExcelReader excelReader) {
        this.termImportRepository = termImportRepository;
        this.excelReader = excelReader;
    }

    public void importTermImports() throws IOException {
        List<TermImport> importedTerms = excelReader.readTermsFromFile(TermImport.class);
        termImportRepository.saveAll(importedTerms);
        System.out.println("Sparade " + importedTerms.size() + " importerade termer i databasen!");
    }

    public TermImport getTermImportByTypeAndCode(String type, String code) {
        return termImportRepository.findByTypeAndCode(type.trim(), code.trim()).orElse(null);
    }

    public List<TermImport> getAllTermImports() {
        return termImportRepository.findAll();
    }

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
