package org.example.controller;

import org.example.model.Term;
import org.example.service.TermService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/api/terms")
public class TermController {

    @Autowired
    private TermService termService;

    @Autowired
    public TermController(TermService termService) {
        this.termService = termService;
    }

    /**
     * Importerar termer från en Excel-fil och sparar dem i Term-repositoryt.
     *
     * @throws IOException om det uppstår ett fel vid läsning av Excel-filen.
     */
    @PostMapping("/import")
    public String importTerms() throws IOException {
        termService.importTerms();  // Kallar tjänstemetoden för import
        return "Termer importerades framgångsrikt!";
    }

    /**
     * Hämtar ett Term-objekt baserat på dess typ och kod.
     *
     * @param type den typ av term att söka efter.
     * @param code den kod för termen att söka efter.
     * @return Term-objektet om det hittas, annars en 404 Not Found.
     */
    @GetMapping("/by-type-and-code")
    public Term getTermByTypeAndCode(@RequestParam String type, @RequestParam String code) {
        Term term = termService.getTermByTypeAndCode(type, code);
        if (term == null) {
            throw new NoSuchElementException("Term not found with type: " + type + " and code: " + code);
        }
        return term;
    }

    /**
     * Hämtar alla Term-objekt.
     *
     * @return en lista med alla Term-objekt.
     */
    @GetMapping
    public List<Term> getAllTerms() {
        return termService.getAllTerms();
    }
}

