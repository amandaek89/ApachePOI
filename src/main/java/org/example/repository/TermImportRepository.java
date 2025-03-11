package org.example.repository;

import org.example.model.TermImport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repository för {@link TermImport} entiteten.
 * Denna interface hanterar databasoperationer för {@link TermImport} objekt.
 */
@Repository
public interface TermImportRepository extends JpaRepository<TermImport, Long> {

    /**
     * Hittar en {@link TermImport} baserat på termen type och code.
     *
     * @param type Termens typ.
     * @param code Termens kod.
     * @return Ett {@link Optional} som innehåller den matchande {@link TermImport}-objektet, om den finns.
     */
    Optional<TermImport> findByTypeAndCode(String type, String code);

}


