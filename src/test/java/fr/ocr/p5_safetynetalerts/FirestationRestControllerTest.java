package fr.ocr.p5_safetynetalerts;

import fr.ocr.p5_safetynetalerts.service.Database;
import fr.ocr.p5_safetynetalerts.exception.DatabaseException;
import fr.ocr.p5_safetynetalerts.exception.ElementNotFoundException;
import fr.ocr.p5_safetynetalerts.model.FirestationModel;
import fr.ocr.p5_safetynetalerts.model.ResponseModel;
import fr.ocr.p5_safetynetalerts.controller.rest.FirestationRestController;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@SpringBootTest
class FirestationRestControllerTest {

    @Autowired
    FirestationRestController firestationRestController;

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
    void addPersonTest() {
        FirestationModel firestationModel = new FirestationModel();
        firestationModel.setAddress("test");

        ResponseEntity<FirestationModel> response = firestationRestController.addFirestation(firestationModel);

        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertTrue(0 < Objects.requireNonNull(response.getBody()).getId());
    }

    @Test
    void addPersonThrowDatabaseException() {
        Assertions.assertThrows(DatabaseException.class, () -> firestationRestController.addFirestation(null));
    }

    @Test
    void updatePersonTest() {
        FirestationModel firestationModel = new FirestationModel();
        firestationModel.setAddress("test");

        ResponseEntity<FirestationModel> response = firestationRestController.addFirestation(firestationModel);

        Map<String, Object> updatedProperties = new HashMap<>();
        updatedProperties.put("address", "updated");


        response = firestationRestController.updateFirestation(updatedProperties, Objects.requireNonNull(response.getBody()).getId());

        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertEquals("updated", Objects.requireNonNull(response.getBody()).getAddress());
    }

    @Test
    void deletePersonTest() {
        FirestationModel firestationModel = new FirestationModel();
        firestationModel.setAddress("test");
        firestationModel.setStation("666");

        firestationRestController.addFirestation(firestationModel);

        firestationRestController.deleteFirestationMapping("666", "test");
        Assertions.assertThrows(ElementNotFoundException.class, () -> firestationRestController.deleteFirestationMapping("666", "test"));
    }

    @Test
    void getPersonCoveredByFirestationTest() {
        ResponseEntity<ResponseModel> response = firestationRestController.getPersonCoveredByFirestation("2");

        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertNotNull(response.getBody());
    }
}
