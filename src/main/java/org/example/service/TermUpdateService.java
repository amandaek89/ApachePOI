package org.example.service;

import org.example.model.Term;
import org.example.model.TermImport;
import org.example.model.TermStatus;
import org.example.repository.TermImportRepository;
import org.example.repository.TermRepository;
import org.example.mapper.TermMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * Serviceklass för att uppdatera, lägga till eller ta bort termer i Term-tabellen.
 * Denna klass hanterar logiken för att behandla termer baserat på deras status,
 * såsom NY, UPPDATERAD, RADERAD eller OFÖRÄNDRAD.
 */
@Service
public class TermUpdateService {

    private final TermRepository termRepository;
    private final TermImportRepository termImportRepository;
    private final TermMapper termMapper;

    /**
     * Konstruktor för att initialisera TermUpdateService med sina beroenden.
     *
     * @param termRepository      repositoryt som används för att hantera Term-objekt.
     * @param termImportRepository repositoryt som används för att hantera TermImport-objekt.
     * @param termMapper          en mapper som omvandlar mellan TermImport och Term.
     */
    @Autowired
    public TermUpdateService(TermRepository termRepository, TermImportRepository termImportRepository, TermMapper termMapper) {
        this.termRepository = termRepository;
        this.termImportRepository = termImportRepository;
        this.termMapper = termMapper;
    }

    /**
     * Sparar, uppdaterar eller tar bort en term baserat på dess status.
     * Metoden utför olika åtgärder beroende på statusen på termen (NY, UPPDATERAD, RADERAD, OFÖRÄNDRAD).
     *
     * @param type termen typ.
     * @param code termen kod.
     */
    public void saveOrUpdateTerm(String type, String code) {
        Optional<Term> existingTerm = termRepository.findByTypeAndCode(type, code);
        Optional<TermImport> termImport = (termImportRepository.findByTypeAndCode(type, code));

        TermStatus status = termImport.map(TermImport::getStatus)
                .orElse(existingTerm.isPresent() ? TermStatus.DELETED : null);

        if (status == null) {
            System.out.println("Ingen åtgärd för termen med type: " + type + " och code: " + code);
            return;
        }

        switch (status) {
            case NEW -> termImport.ifPresent(this::addNewTerm);
            case UPDATED -> existingTerm.ifPresent(term -> termImport.ifPresent(imported -> updateExistingTerm(term, imported)));
            case DELETED -> existingTerm.ifPresent(this::deleteExistingTerm);
            case UNCHANGED -> System.out.println("Oförändrad term: " + status);
            default -> System.out.println("Okänd status: " + status);
        }
    }

    /**
     * Lägger till en ny term i Term-tabellen.
     *
     * @param termImport termen som ska läggas till.
     */
    private void addNewTerm(TermImport termImport) {
        Term newTerm = termMapper.convertToTerm(termImport);
        termRepository.save(newTerm);
        System.out.println("Ny term har lagts till i Term-tabellen: " + newTerm);
    }

    /**
     * Uppdaterar en befintlig term i Term-tabellen med värden från TermImport.
     *
     * @param term       den befintliga termen som ska uppdateras.
     * @param termImport de nya värdena från TermImport.
     */
    private void updateExistingTerm(Term term, TermImport termImport) {
        term.setText(termImport.getText());
        term.setEgNumber(termImport.getEgNumber());
        term.setVehicleType(termImport.getVehicleType());
        termRepository.save(term);
        System.out.println("Termen har uppdaterats i Term-tabellen: " + term);
    }

    /**
     * Tar bort en befintlig term från Term-tabellen.
     *
     * @param term termen som ska raderas.
     */
    private void deleteExistingTerm(Term term) {
        termRepository.delete(term);
        System.out.println("Termen har raderats från Term-tabellen: " + term);
    }
}
