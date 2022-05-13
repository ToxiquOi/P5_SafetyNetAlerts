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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
public class AlertRestController extends AbstractRestExceptionHandler {

    private final PersonDao personDao;
    private final MedicalRecordDao medicalRecordDao;
    private final FireStationDao fireStationDao;
    private final CalculatorService calculatorService;

    @Autowired
    public AlertRestController(PersonDao personDao, MedicalRecordDao medicalRecordDao, FireStationDao fireStationDao, CalculatorService calculatorService) {
        this.personDao = personDao;
        this.medicalRecordDao = medicalRecordDao;
        this.fireStationDao = fireStationDao;
        this.calculatorService = calculatorService;
    }

    @SneakyThrows
    @GetMapping("childAlert")
    public ResponseEntity<ResponseModel> childAlert(@RequestParam String address) {
        checkIfNotNull(address);
        Map<String, String> attr = new HashMap<>();
        attr.put("address", address);


        List<ResponseModel> childList = new ArrayList<>();


        List<PersonModel> personModels = personDao.reads(attr);

        for (PersonModel personModel : personModels) {
            attr.clear();
            attr.put("lastname", personModel.getLastName());
            attr.put("firstname", personModel.getFirstName());

            MedicalRecordModel medicalRecord = medicalRecordDao.reads(attr).get(0);
            if (calculatorService.caculateYearsOld(medicalRecord.getBirthdate()) <= 18) {

                ResponseModel child = new ResponseModel();
                child.put("firstName", personModel.getFirstName());
                child.put("lastName", personModel.getLastName());
                child.put("age", calculatorService.caculateYearsOld(medicalRecord.getBirthdate()));
                child.put("allergies", medicalRecord.getAllergies());
                child.put("medications", medicalRecord.getMedications());
                child.put("family", personModels
                        .stream()
                        .filter(p -> p.getLastName().equals(personModel.getLastName()) && !p.getFirstName().equals(personModel.getFirstName()))
                        .toList()
                );

                childList.add(child);
            }
        }

        ResponseModel rsModel = new ResponseModel();
        rsModel.put("address", address);
        rsModel.put("childs", childList);

        return ResponseEntity.ok(rsModel);
    }

    @SneakyThrows
    @GetMapping("phoneAlert")
    public ResponseEntity<ResponseModel> phoneAlert(@RequestParam String station) {
        checkIfNotNull(station);
        Map<String, String> attr = new HashMap<>();
        attr.put("station", station);

        ResponseModel rsModel = new ResponseModel();
        List<String> phoneNumbers = new ArrayList<>();

        for (FirestationModel firestationModel : fireStationDao.reads(attr)) {
            attr.clear();
            attr.put("address", firestationModel.getAddress());

            personDao.reads(attr)
                    .forEach(personModel -> phoneNumbers.add(personModel.getPhone()));
        }

        rsModel.put("station", station);
        rsModel.put("phones", phoneNumbers);

        return ResponseEntity.ok(rsModel);
    }
}
