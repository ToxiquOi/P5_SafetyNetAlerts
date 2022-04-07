package fr.ocr.p5_safetynetalerts.exception;

import lombok.extern.log4j.Log4j2;

@Log4j2
public class ElementNotFoundException extends Exception {
    public ElementNotFoundException(String message) {
        super(message);
        log.warn(message + ": " + this.getLocalizedMessage(), this);
    }
}
