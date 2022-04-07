package fr.ocr.p5_safetynetalerts.controller.rest;

import fr.ocr.p5_safetynetalerts.dao.FireStationDao;
import fr.ocr.p5_safetynetalerts.dao.MedicalRecordDao;
import fr.ocr.p5_safetynetalerts.dao.PersonDao;
import fr.ocr.p5_safetynetalerts.model.FirestationModel;
import fr.ocr.p5_safetynetalerts.model.MedicalRecordModel;
import fr.ocr.p5_safetynetalerts.model.PersonModel;
import fr.ocr.p5_safetynetalerts.model.ResponseModel;
import fr.ocr.p5_safetynetalerts.service.CalculatorService;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("fire")
public class FireRestController extends AbstractRestExceptionHandler {

    private final PersonDao personDao;
    private final MedicalRecordDao medicalRecordDao;
    private final FireStationDao fireStationDao;
    private final CalculatorService calculatorService;

    @Autowired
    public FireRestController(PersonDao personDao,
                              MedicalRecordDao medicalRecordDao,
                              FireStationDao fireStationDao,
                              CalculatorService calculatorService) {
        this.personDao = personDao;
        this.medicalRecordDao = medicalRecordDao;
        this.fireStationDao = fireStationDao;
        this.calculatorService = calculatorService;
    }

    @SneakyThrows
   @GetMapping
    public ResponseEntity<ResponseModel> getPersonAndFirestationFromAddress(@RequestParam String address) {
        checkIfNotNull(address);
        Map<String, String> attr = new HashMap<>();
        attr.put("address", address);

        FirestationModel station = fireStationDao.reads(attr).get(0);
        ResponseModel rsModel = new ResponseModel();
        rsModel.put("station", station.getStation());

        for (PersonModel personModel : personDao.reads(attr)) {
            attr.clear();
            attr.put("firstname", personModel.getFirstName());
            attr.put("lastname", personModel.getLastName());

            MedicalRecordModel medicalRecordModel = medicalRecordDao.reads(attr).get(0);

            ResponseModel medicalInfo = new ResponseModel();
            medicalInfo.put("age", calculatorService.caculateYearsOld(medicalRecordModel.getBirthdate()));
            medicalInfo.put("medications", medicalRecordModel.getMedications());
            medicalInfo.put("allergies", medicalRecordModel.getAllergies());

            rsModel.put(personModel.getLastName() + " " + personModel.getFirstName(), medicalInfo);
        }


        return ResponseEntity.ok(rsModel);
    }
}
