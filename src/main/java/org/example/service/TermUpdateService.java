package org.example.service;

import org.example.model.Term;
import org.example.model.TermImport;
import org.example.model.TermStatus;
import org.example.repository.TermRepository;
import org.example.service.TermImportService;
import org.example.service.TermService;
import org.example.mapper.TermMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class TermUpdateService {

    private final TermRepository termRepository;
    private final TermService termService;
    private final TermImportService termImportService;
    private final TermMapper termMapper;

    @Autowired
    public TermUpdateService(TermRepository termRepository, TermService termService,
                             TermImportService termImportService, TermMapper termMapper) {
        this.termRepository = termRepository;
        this.termService = termService;
        this.termImportService = termImportService;
        this.termMapper = termMapper;
    }

    /**
     * Hämtar en term från Term-tabellen eller konverterar en från TermImport om den inte finns i Term.
     */
    public Optional<Term> getTermFromBothTables(String type, String code) {
        return Optional.ofNullable(termService.getTermByTypeAndCode(type, code))
                .or(() -> Optional.ofNullable(termImportService.getTermImportByTypeAndCode(type, code))
                        .map(termMapper::convertToTerm));
    }

    /**
     * Sparar, uppdaterar eller tar bort en term baserat på dess status.
     */
    public void saveOrUpdateTerm(String type, String code) {
        Optional<Term> existingTerm = termRepository.findByTypeAndCode(type, code);
        Optional<TermImport> termImport = Optional.ofNullable(termImportService.getTermImportByTypeAndCode(type, code));

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
     */
    private void addNewTerm(TermImport termImport) {
        Term newTerm = termMapper.convertToTerm(termImport);
        termRepository.save(newTerm);
        System.out.println("Ny term har lagts till i Term-tabellen: " + newTerm);
    }

    /**
     * Uppdaterar en befintlig term i Term-tabellen med värden från TermImport.
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
     */
    private void deleteExistingTerm(Term term) {
        termRepository.delete(term);
        System.out.println("Termen har raderats från Term-tabellen: " + term);
    }
}
