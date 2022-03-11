package fr.ocr.p5_safetynetalerts;

import fr.ocr.p5_safetynetalerts.dao.MedicalRecordDao;
import fr.ocr.p5_safetynetalerts.database.Database;
import fr.ocr.p5_safetynetalerts.exception.DatabaseException;
import fr.ocr.p5_safetynetalerts.exception.ElementNotFoundException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.HashMap;
import java.util.Map;

@SpringBootTest
class MedicalRecordDaoTest {
    @Autowired
    private Database db;

    @Autowired
    private MedicalRecordDao medicalRecordDao;

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

        Assertions.assertEquals(1, medicalRecordDao.reads(attributes).size());
        medicalRecordDao.deleteByFirstNameAndLastName("Ron", "Peters");
        Assertions.assertEquals(0, medicalRecordDao.reads(attributes).size());
    }
}
