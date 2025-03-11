package org.example.service;

import org.example.model.Term;
import org.example.repository.TermImportRepository;
import org.example.repository.TermRepository;
import org.example.util.ExcelReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.io.IOException;
import java.util.List;

@Service
public class TermService {

    private final TermRepository termRepository;

    private final ExcelReader excelReader;

    @Autowired
    public TermService(TermRepository termRepository, ExcelReader excelReader) {
        this.termRepository = termRepository;
        this.excelReader = excelReader;
    }

    public void importTerms() throws IOException {
        List<Term> terms = excelReader.readTermsFromFile(Term.class);
        termRepository.saveAll(terms);
        System.out.println("Sparade " + terms.size() + " termer i databasen!");
    }

    public Term getTermByTypeAndCode(String type, String code) {
        return termRepository.findByTypeAndCode(type.trim(), code.trim()).orElse(null);
    }

    public List<Term> getAllTerms() {
        return termRepository.findAll();
    }
}
