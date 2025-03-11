package org.example.model;

/**
 * Enum som representerar status för en term.
 * Används för att indikera termens tillstånd i relation till databasen eller importprocessen.
 */
public enum TermStatus {
    /**
     * Termen är ny och finns endast i importtabellen.
     */
    NEW,

    /**
     * Termen har uppdaterats och bör reflektera den senaste versionen.
     */
    UPDATED,

    /**
     * Termen har tagits bort och ska raderas från databasen.
     */
    DELETED,

    /**
     * Termen är oförändrad och har inte modifierats.
     */
    UNCHANGED // För att tydliggöra status innan jämförelse
}
