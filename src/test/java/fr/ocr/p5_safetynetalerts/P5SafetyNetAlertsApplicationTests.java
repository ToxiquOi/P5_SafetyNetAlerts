package fr.ocr.p5_safetynetalerts;

import fr.ocr.p5_safetynetalerts.dao.FireStationDao;
import fr.ocr.p5_safetynetalerts.dao.MedicalRecordDao;
import fr.ocr.p5_safetynetalerts.dao.PersonDao;
import fr.ocr.p5_safetynetalerts.database.Database;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class P5SafetyNetAlertsApplicationTests {

    private final Database database;
    private final FireStationDao fireStationDao;
    private final MedicalRecordDao medicalRecordDao;
    private final PersonDao personDao;

    @Autowired
    public P5SafetyNetAlertsApplicationTests(Database database, FireStationDao fireStationDao, MedicalRecordDao medicalRecordDao, PersonDao personDao) {
        this.database = database;
        this.fireStationDao = fireStationDao;
        this.medicalRecordDao = medicalRecordDao;
        this.personDao = personDao;
    }

    @Test
    void contextLoads() {
        Assertions.assertNotNull(database);
        Assertions.assertNotNull(fireStationDao);
        Assertions.assertNotNull(medicalRecordDao);
        Assertions.assertNotNull(personDao);
    }

}
