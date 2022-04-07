package fr.ocr.p5_safetynetalerts.exception;

import lombok.extern.log4j.Log4j2;

@Log4j2
public class ArgumentNullException extends Exception {
    public ArgumentNullException(String message) {
        super(message);
        log.error(this.getLocalizedMessage(), this);
    }
}
