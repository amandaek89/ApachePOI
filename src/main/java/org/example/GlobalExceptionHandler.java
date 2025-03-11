package org.example;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.NoSuchElementException;


/**
 * Global Exception Handler för att fånga och hantera olika typer av undantag.
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    /**
     * Hanterar RuntimeException och returnerar ett 500 Internal Server Error svar.
     *
     * @param ex undantaget som kastades.
     * @return ett svar med HTTP-status 500 och felmeddelande.
     */
    @ExceptionHandler(RuntimeException.class)
    public ResponseEntity<String> handleRuntimeException(RuntimeException ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("An internal error occurred. Please try again later. Details: " + ex.getMessage());
    }

    /**
     * Hanterar IllegalArgumentException och returnerar ett 400 Bad Request svar.
     *
     * @param ex undantaget som kastades.
     * @return ett svar med HTTP-status 400 och felmeddelande.
     */
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleIllegalArgument(IllegalArgumentException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body("Invalid input provided. Please check your request. Details: " + ex.getMessage());
    }

    /**
     * Hanterar NoSuchElementException och returnerar ett 404 Not Found svar.
     *
     * @param ex undantaget som kastades.
     * @return ett svar med HTTP-status 404 och felmeddelande.
     */
    @ExceptionHandler(NoSuchElementException.class)
    public ResponseEntity<String> handleNoSuchElementException(NoSuchElementException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body("Resource not found. Details: " + ex.getMessage());
    }
}