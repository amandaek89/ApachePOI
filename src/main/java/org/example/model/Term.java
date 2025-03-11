package org.example.model;

import jakarta.persistence.*;
import lombok.*;

/**
 * Representerar en term i systemet.
 * Denna klass används för att mappa termer från Term-tabellen i databasen.
 */
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Term {

    /**
     * Unikt identifierare för termen.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Typen på termen.
     */
    private String type;

    /**
     * Koden för termen.
     */
    private String code;

    /**
     * Texten för termen.
     */
    private String text;

    /**
     * EG-nummer associerat med termen.
     */
    private String egNumber;

    /**
     * Fordonstypen associerad med termen.
     */
    private String vehicleType;

    /**
     * Status för termen (NEW, UPDATED, DELETED, UNCHANGED).
     */
    @Enumerated(EnumType.STRING)
    private TermStatus status;
}
