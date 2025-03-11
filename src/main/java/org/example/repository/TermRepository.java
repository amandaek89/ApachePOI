package org.example.repository;

import org.example.model.Term;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repository för {@link Term} entiteten.
 * Denna interface hanterar databasoperationer för {@link Term} objekt.
 */
@Repository
public interface TermRepository extends JpaRepository<Term, Long> {

    /**
     * Hittar en {@link Term} baserat på termen type och code.
     *
     * @param type Termens typ.
     * @param code Termens kod.
     * @return Ett {@link Optional} som innehåller den matchande {@link Term}-objektet, om den finns.
     */
    Optional<Term> findByTypeAndCode(String type, String code);

}


