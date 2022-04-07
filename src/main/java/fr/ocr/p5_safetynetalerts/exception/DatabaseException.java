package fr.ocr.p5_safetynetalerts.exception;

import lombok.extern.log4j.Log4j2;

@Log4j2
public class DatabaseException extends Exception {
    public DatabaseException(String message) {
        super(message);
        log.warn(this.getLocalizedMessage());
    }

    public DatabaseException(String message, Throwable cause) {
        this(message);
        log.debug(cause.getLocalizedMessage());
    }
}
