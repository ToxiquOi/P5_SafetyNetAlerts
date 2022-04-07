package fr.ocr.p5_safetynetalerts;

import fr.ocr.p5_safetynetalerts.service.Database;
import fr.ocr.p5_safetynetalerts.exception.ArgumentNullException;
import fr.ocr.p5_safetynetalerts.model.ResponseModel;
import fr.ocr.p5_safetynetalerts.rest.AlertRestController;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;


@SpringBootTest
class AlertRestControllerTest {

    @Autowired
    AlertRestController alertRestController;

    @Autowired
    Database database;

    @BeforeEach
    private void BeforePerTest() {
        database.loadData(getClass().getClassLoader().getResourceAsStream("data.json"));
    }

    @AfterEach
    private void AfterPerTest() {
        database.truncate();
    }

    @Test
    void childAlertTest() {
        ResponseEntity<ResponseModel> response = alertRestController.childAlert("1509 Culver St");
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertNotNull(response.getBody());
    }

    @Test
    void addPersonThrowDatabaseException() {
        Assertions.assertThrows(ArgumentNullException.class, () -> alertRestController.childAlert(null));
    }

    @Test
    void phoneAlertTest() {
        ResponseEntity<ResponseModel> response = alertRestController.phoneAlert("2");
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertNotNull(response.getBody());
    }
}
