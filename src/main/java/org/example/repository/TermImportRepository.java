package org.example.repository;

import org.example.model.TermImport;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TermImportRepository extends JpaRepository<TermImport, Long> {

    Optional<TermImport> findByTypeAndCode(String type, String code);

}

