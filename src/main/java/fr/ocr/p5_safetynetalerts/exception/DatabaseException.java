package fr.ocr.p5_safetynetalerts.exception;

import lombok.extern.log4j.Log4j2;

@Log4j2
public class DatabaseException extends Exception {
    public DatabaseException(String message) {
        super(message);
        log.error(message + ": " + this.getLocalizedMessage(), this);
    }

    public DatabaseException(String message, Throwable cause) {
        this(message);
        log.error(message + ": " + this.getLocalizedMessage(), cause);
    }
}
