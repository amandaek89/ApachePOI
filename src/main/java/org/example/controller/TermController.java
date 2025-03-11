package org.example.controller;

import org.example.service.TermService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/terms")
public class TermController {

    @Autowired
    private TermService termService;

    @PostMapping("/import")
    public String importTerms() {
        try {
            termService.importTerms();
            return "Import från Excel lyckades!";
        } catch (IOException e) {
            return "Fel vid import: " + e.getMessage();
        }
    }
}

