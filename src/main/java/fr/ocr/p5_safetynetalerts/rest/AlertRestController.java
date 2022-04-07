package fr.ocr.p5_safetynetalerts.rest;

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
@RequestMapping("alert")
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
    @GetMapping("/child")
    public ResponseEntity<ResponseModel> childAlert(@RequestParam String address) {
        checkIfNotNull(address);
        Map<String, String> attr = new HashMap<>();
        attr.put("address", address);


        List<ResponseModel> personList = new ArrayList<>();
        List<PersonModel> personModels = personDao.reads(attr);

        for (PersonModel personModel : personModels) {
            attr.clear();
            attr.put("lastname", personModel.getLastName());
            attr.put("firstname", personModel.getFirstName());

            MedicalRecordModel medicalRecord = medicalRecordDao.reads(attr).get(0);

            if (18 > calculatorService.caculateYearsOld(medicalRecord.getBirthdate())) {
                ResponseModel personResponse = new ResponseModel();
                personResponse.put("firstName", personModel.getFirstName());
                personResponse.put("lastName", personModel.getLastName());
                personResponse.put("age", calculatorService.caculateYearsOld(medicalRecord.getBirthdate()));
                personResponse.put("allergies", medicalRecord.getAllergies());
                personResponse.put("medications", medicalRecord.getMedications());
                personResponse.put("family", personModels
                        .stream()
                        .filter(p -> p.getLastName().equals(personModel.getLastName()) && !p.getFirstName().equals(personModel.getFirstName()))
                        .toList()
                );
                personList.add(personResponse);
            }
        }

        ResponseModel rsModel = new ResponseModel();
        rsModel.put(address, personList);

        return ResponseEntity.ok(rsModel);
    }

    @SneakyThrows
    @GetMapping("/phone")
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

        rsModel.put(station, phoneNumbers);

        return ResponseEntity.ok(rsModel);
    }
}
