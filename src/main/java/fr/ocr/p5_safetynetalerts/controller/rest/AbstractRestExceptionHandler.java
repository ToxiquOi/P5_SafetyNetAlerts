package fr.ocr.p5_safetynetalerts.controller.rest;

import fr.ocr.p5_safetynetalerts.exception.ArgumentNullException;
import fr.ocr.p5_safetynetalerts.exception.DatabaseException;
import fr.ocr.p5_safetynetalerts.exception.ElementNotFoundException;
import fr.ocr.p5_safetynetalerts.model.ResponseModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;

public abstract class AbstractRestExceptionHandler {

    @ExceptionHandler(ElementNotFoundException.class)
    public ResponseEntity<ResponseModel> handleElementNotFound(ElementNotFoundException ex) {
        ResponseModel responseModel = new ResponseModel();
        responseModel.put("error", ex.getMessage());

        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(responseModel);
    }

    @ExceptionHandler({DatabaseException.class})
    public ResponseEntity<ResponseModel> handleDatabaseException(DatabaseException ex) {
        ResponseModel responseModel = new ResponseModel();
        responseModel.put("error", ex.getMessage());

        return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(responseModel);
    }

    @ExceptionHandler({ArgumentNullException.class})
    public ResponseEntity<ResponseModel> handleArgumentNullException(ArgumentNullException ex) {
        ResponseModel responseModel = new ResponseModel();
        responseModel.put("error", ex.getMessage());

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(responseModel);
    }

    protected void checkIfNotNull(Object ...args) throws ArgumentNullException {
        for(Object arg : args) {
            if(arg == null)
                throw new ArgumentNullException("Argument request cannot be null");
        }
    }
}
