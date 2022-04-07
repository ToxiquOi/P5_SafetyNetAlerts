package fr.ocr.p5_safetynetalerts;

import fr.ocr.p5_safetynetalerts.dao.FireStationDao;
import fr.ocr.p5_safetynetalerts.dao.MedicalRecordDao;
import fr.ocr.p5_safetynetalerts.dao.PersonDao;
import fr.ocr.p5_safetynetalerts.service.Database;
import fr.ocr.p5_safetynetalerts.rest.*;
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
    private final AlertRestController alertRestController;
    private final CommunityEmailRestController communityEmailRestController;
    private final FireRestController fireRestController;
    private final FirestationRestController firestationRestController;
    private final FloodRestController floodRestController;
    private final MedicalRecordRestController medicalRecordRestController;
    private final PersonInfoRestController personInfoRestController;
    private final PersonRestController personRestController;

    @Autowired
    public P5SafetyNetAlertsApplicationTests(Database database, FireStationDao fireStationDao,
                                             MedicalRecordDao medicalRecordDao, PersonDao personDao,
                                             AlertRestController alertRestController, CommunityEmailRestController communityEmailRestController,
                                             FireRestController fireRestController, FirestationRestController firestationRestController,
                                             FloodRestController floodRestController, MedicalRecordRestController medicalRecordRestController,
                                             PersonInfoRestController personInfoRestController, PersonRestController personRestController) {
        this.database = database;
        this.fireStationDao = fireStationDao;
        this.medicalRecordDao = medicalRecordDao;
        this.personDao = personDao;
        this.alertRestController = alertRestController;
        this.communityEmailRestController = communityEmailRestController;
        this.fireRestController = fireRestController;
        this.firestationRestController = firestationRestController;
        this.floodRestController = floodRestController;
        this.medicalRecordRestController = medicalRecordRestController;
        this.personInfoRestController = personInfoRestController;
        this.personRestController = personRestController;
    }

    @Test
    void contextLoads() {
        Assertions.assertNotNull(database);
        Assertions.assertNotNull(fireStationDao);
        Assertions.assertNotNull(medicalRecordDao);
        Assertions.assertNotNull(personDao);
        Assertions.assertNotNull(alertRestController);
        Assertions.assertNotNull(communityEmailRestController);
        Assertions.assertNotNull(fireRestController);
        Assertions.assertNotNull(firestationRestController);
        Assertions.assertNotNull(floodRestController);
        Assertions.assertNotNull(medicalRecordRestController);
        Assertions.assertNotNull(personInfoRestController);
        Assertions.assertNotNull(personRestController);
    }

}
