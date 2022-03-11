package fr.ocr.p5_safetynetalerts.rest;

import fr.ocr.p5_safetynetalerts.exception.DatabaseException;
import fr.ocr.p5_safetynetalerts.exception.ElementNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;

public abstract class AbstractRestExceptionHandler {
    @ExceptionHandler(ElementNotFoundException.class)
    public ResponseEntity<String> handleElementNotFound(ElementNotFoundException ex) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
    }

    @ExceptionHandler({DatabaseException.class})
    public ResponseEntity<String> handleDatabaseException(DatabaseException ex) {
        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(ex.getMessage());
    }
}
