package fr.ocr.p5_safetynetalerts;

import fr.ocr.p5_safetynetalerts.dao.PersonDao;
import fr.ocr.p5_safetynetalerts.database.Database;
import fr.ocr.p5_safetynetalerts.exception.ArgumentNullException;
import fr.ocr.p5_safetynetalerts.exception.DatabaseException;
import fr.ocr.p5_safetynetalerts.exception.ElementNotFoundException;
import fr.ocr.p5_safetynetalerts.model.MedicalRecordModel;
import fr.ocr.p5_safetynetalerts.model.PersonModel;
import fr.ocr.p5_safetynetalerts.rest.MedicalRecordRestController;
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
public class MedicalRecordRestControllerTest {

    @Autowired
    private MedicalRecordRestController medicalRecordRestController;


    @Autowired
    private Database db;

    @BeforeEach
    private void BeforePerTest() {
        db.loadData(getClass().getClassLoader().getResourceAsStream("data.json"));
    }

    @AfterEach
    private void AfterPerTest() {
        db.truncate();
    }

    @Test
    void addMedicalRecordTest() {
        MedicalRecordModel medicalRecordModel = new MedicalRecordModel();
        medicalRecordModel.setFirstName("test");
        medicalRecordModel.setLastName("test");

        ResponseEntity<MedicalRecordModel> response = medicalRecordRestController.addMedicalRecord(medicalRecordModel);

        Assertions.assertEquals(response.getStatusCode(), HttpStatus.OK);
        Assertions.assertTrue(0 < Objects.requireNonNull(response.getBody()).getId());
    }

    @Test
    void addPersonThrowDatabaseException() {
        Assertions.assertThrows(ArgumentNullException.class, () -> medicalRecordRestController.addMedicalRecord(null));
    }

    @Test
    void updateMedicalRecordTest() {
        MedicalRecordModel medicalRecordModel = new MedicalRecordModel();
        medicalRecordModel.setFirstName("test");
        medicalRecordModel.setLastName("test");

        ResponseEntity<MedicalRecordModel> response = medicalRecordRestController.addMedicalRecord(medicalRecordModel);

        Map<String, Object> updatedProperties = new HashMap<>();
        updatedProperties.put("firstname", "updated");
        updatedProperties.put("lastname", "updated");

        response = medicalRecordRestController.updateMedicalRecord(updatedProperties, Objects.requireNonNull(response.getBody()).getId());

        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertEquals("updated", Objects.requireNonNull(response.getBody()).getFirstName());
        Assertions.assertEquals("updated", Objects.requireNonNull(response.getBody()).getLastName());
    }

    @Test
    void deletePersonTest() {
        MedicalRecordModel medicalRecordModel = new MedicalRecordModel();
        medicalRecordModel.setFirstName("test");
        medicalRecordModel.setLastName("test");

        medicalRecordRestController.addMedicalRecord(medicalRecordModel);

        medicalRecordRestController.deleteMedicalRecord("test", "test");
        Assertions.assertThrows(ElementNotFoundException.class, () -> medicalRecordRestController.deleteMedicalRecord("test", "test"));
    }



}
