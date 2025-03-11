package org.example.mapper;

import org.example.model.Term;
import org.example.model.TermImport;
import org.springframework.stereotype.Component;

@Component
public class TermMapper {

    public Term convertToTerm(TermImport termImport) {
        if (termImport == null) {
            return null;
        }

        return new Term(
                termImport.getId(),
                termImport.getType(),
                termImport.getCode(),
                termImport.getText(),
                termImport.getEgNumber(),   // Sätts till null om det saknas i TermImport
                termImport.getVehicleType(), // Sätts till null om det saknas i TermImport
                termImport.getStatus()
        );
    }

    public TermImport convertToTermImport(Term term) {
        if (term == null) {
            return null;
        }

        return new TermImport(
                term.getId(),
                term.getType(),
                term.getCode(),
                term.getText(),
                term.getEgNumber(),
                term.getVehicleType(),
                term.getStatus()
        );
    }
}

