package org.example.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Representerar en term i importtabellen (TermImport).
 * Denna klass används för att mappa termer som importeras från externa källor.
 */
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TermImport {

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
     * EG-nummer associerat med termen.
     */
    private String egNumber;

    /**
     * Texten för termen.
     */
    private String text;

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
