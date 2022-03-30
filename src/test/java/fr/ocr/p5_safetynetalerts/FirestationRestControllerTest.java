package fr.ocr.p5_safetynetalerts;

import fr.ocr.p5_safetynetalerts.database.Database;
import fr.ocr.p5_safetynetalerts.exception.DatabaseException;
import fr.ocr.p5_safetynetalerts.model.FirestationModel;
import fr.ocr.p5_safetynetalerts.rest.FirestationRestController;
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
public class FirestationRestControllerTest {

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

        Assertions.assertEquals(response.getStatusCode(), HttpStatus.OK);
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


    }
}
