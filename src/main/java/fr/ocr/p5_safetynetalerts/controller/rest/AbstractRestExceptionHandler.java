package fr.ocr.p5_safetynetalerts.controller.rest;

import fr.ocr.p5_safetynetalerts.exception.ArgumentNullException;
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

    @ExceptionHandler({ArgumentNullException.class})
    public ResponseEntity<String> handleArgumentNullException(ArgumentNullException ex) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    }

    protected void checkIfNotNull(Object ...args) throws ArgumentNullException {
        for(Object arg : args) {
            if(arg == null)
                throw new ArgumentNullException("Argument request cannot be null");
        }
    }
}
