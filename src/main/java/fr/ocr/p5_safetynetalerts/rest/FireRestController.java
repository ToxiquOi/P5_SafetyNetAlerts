package fr.ocr.p5_safetynetalerts.rest;

import fr.ocr.p5_safetynetalerts.dao.FireStationDao;
import fr.ocr.p5_safetynetalerts.dao.MedicalRecordDao;
import fr.ocr.p5_safetynetalerts.dao.PersonDao;
import fr.ocr.p5_safetynetalerts.model.ResponseModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("fire")
public class FireRestController extends AbstractRestExceptionHandler {

    private final PersonDao personDao;
    private final MedicalRecordDao medicalRecordDao;
    private final FireStationDao fireStationDao;

    @Autowired
    public FireRestController(PersonDao personDao, MedicalRecordDao medicalRecordDao, FireStationDao fireStationDao) {
        this.personDao = personDao;
        this.medicalRecordDao = medicalRecordDao;
        this.fireStationDao = fireStationDao;
    }

    @GetMapping
    public ResponseEntity<ResponseModel> getPersonAndFirestationFromAddress(@RequestParam String address) {
        /*
            TODO:
            Person: address -> nom, prenom
            MedicalReport: nom, prenom -> age, medicaments, alergies
            FireStation: address -> numéro
         */

        return ResponseEntity.ok(null);
    }
}
