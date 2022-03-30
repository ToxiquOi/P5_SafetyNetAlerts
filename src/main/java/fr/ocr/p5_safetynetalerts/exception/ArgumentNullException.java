package fr.ocr.p5_safetynetalerts.exception;

public class ArgumentNullException extends Exception {
    public ArgumentNullException(String message) {
        super(message);
    }

    public ArgumentNullException(String message, Throwable cause) {
        super(message, cause);
    }
}
