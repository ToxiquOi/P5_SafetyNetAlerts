package fr.ocr.p5_safetynetalerts;

import fr.ocr.p5_safetynetalerts.component.AbstractModelTestImp;
import fr.ocr.p5_safetynetalerts.dao.PersonDao;
import fr.ocr.p5_safetynetalerts.database.Database;
import fr.ocr.p5_safetynetalerts.exception.DatabaseException;
import fr.ocr.p5_safetynetalerts.exception.ElementNotFoundException;
import fr.ocr.p5_safetynetalerts.rest.PersonRestController;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.HashMap;
import java.util.Map;

@SpringBootTest
class PersonDaoTest {

    @Autowired
    private Database db;

    @Autowired
    private PersonDao personDao;

    @BeforeEach
    private void BeforePerTest() {
        db.loadData(getClass().getClassLoader().getResourceAsStream("data.json"));
    }

    @AfterEach
    private void AfterPerTest() {
        db.truncate();
    }

    @Test
    void testDeleteByFirstnameAndLastName() throws DatabaseException, ElementNotFoundException {
        Map<String, String> attributes = new HashMap<>();
        attributes.put("firstName", "Ron");
        attributes.put("lastName", "Peters");

        Assertions.assertEquals(1, personDao.reads(attributes).size());
        personDao.deleteByFirstNameAndLastName("Ron", "Peters");
        Assertions.assertEquals(0, personDao.reads(attributes).size());
    }

}
