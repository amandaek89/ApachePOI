package org.example.mapper;

import org.example.model.Term;
import org.example.model.TermImport;
import org.springframework.stereotype.Component;

/**
 * Mapper-klass för att konvertera mellan {@link Term} och {@link TermImport}.
 * Används för att översätta objekt från importtabellen till Term-tabellen och vice versa.
 */
@Component
public class TermMapper {

    /**
     * Konverterar ett {@link TermImport} objekt till ett {@link Term} objekt.
     *
     * @param termImport TermImport-objektet som ska konverteras.
     * @return Det konverterade {@link Term}-objektet, eller null om {@code termImport} är null.
     */
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

    /**
     * Konverterar ett {@link Term} objekt till ett {@link TermImport} objekt.
     *
     * @param term Term-objektet som ska konverteras.
     * @return Det konverterade {@link TermImport}-objektet, eller null om {@code term} är null.
     */
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
