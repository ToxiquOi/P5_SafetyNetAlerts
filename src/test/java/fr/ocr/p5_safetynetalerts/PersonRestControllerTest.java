package fr.ocr.p5_safetynetalerts;

import fr.ocr.p5_safetynetalerts.service.Database;
import fr.ocr.p5_safetynetalerts.exception.ArgumentNullException;
import fr.ocr.p5_safetynetalerts.exception.ElementNotFoundException;
import fr.ocr.p5_safetynetalerts.model.PersonModel;
import fr.ocr.p5_safetynetalerts.controller.rest.PersonRestController;
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
class PersonRestControllerTest {

    @Autowired
    PersonRestController personRestController;

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
        PersonModel personModel = new PersonModel();
        personModel.setFirstName("test");
        personModel.setLastName("test");
        personModel.setAddress("test");
        personModel.setEmail("test");
        personModel.setPhone("test");

        ResponseEntity<PersonModel> response = personRestController.addPerson(personModel);

        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertTrue(0 < Objects.requireNonNull(response.getBody()).getId());
    }

    @Test
    void addPersonThrowDatabaseException() {
        Assertions.assertThrows(ArgumentNullException.class, () -> personRestController.addPerson(null));
    }

    @Test
    void updatePersonTest() {
        PersonModel personModel = new PersonModel();
        personModel.setFirstName("test");
        personModel.setLastName("test");
        personModel.setAddress("test");
        personModel.setEmail("test");
        personModel.setPhone("test");

        ResponseEntity<PersonModel> response = personRestController.addPerson(personModel);

        Map<String, Object> updatedProperties = new HashMap<>();
        updatedProperties.put("firstname", "updated");
        updatedProperties.put("lastname", "updated");

        response = personRestController.updatePerson(updatedProperties, Objects.requireNonNull(response.getBody()).getId());

        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());
        Assertions.assertEquals("updated", Objects.requireNonNull(response.getBody()).getFirstName());
        Assertions.assertEquals("updated", Objects.requireNonNull(response.getBody()).getLastName());
    }

    @Test
    void deletePersonTest() {
        PersonModel personModel = new PersonModel();
        personModel.setFirstName("test");
        personModel.setLastName("test");
        personModel.setAddress("test");
        personModel.setEmail("test");
        personModel.setPhone("test");

        personRestController.addPerson(personModel);

        personRestController.deletePerson("test", "test");
        Assertions.assertThrows(ElementNotFoundException.class, () -> personRestController.deletePerson("test", "test"));
    }



}
