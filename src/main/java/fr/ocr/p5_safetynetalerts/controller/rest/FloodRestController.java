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

import javax.validation.constraints.NotNull;
import java.util.*;

@RestController
@RequestMapping("flood")
public class FloodRestController extends AbstractRestExceptionHandler {

    private final FireStationDao fireStationDao;
    private final PersonDao personDao;
    private final MedicalRecordDao medicalRecordDao;
    private final CalculatorService calculatorService;

    @Autowired
    public FloodRestController(FireStationDao fireStationDao,
                               PersonDao personDao,
                               MedicalRecordDao medicalRecordDao,
                               CalculatorService calculatorService) {
        this.fireStationDao = fireStationDao;
        this.personDao = personDao;
        this.medicalRecordDao = medicalRecordDao;
        this.calculatorService = calculatorService;
    }


    @SneakyThrows
    @GetMapping( "/stations")
    public ResponseEntity<List<ResponseModel>> getHomeDependingFromFireStation(@NotNull @RequestParam List<String> stations) {
        checkIfNotNull(stations);

        Map<String, String> attr = new HashMap<>();

        List<ResponseModel> responseList = new ArrayList<>();
        for (String station : stations) {
            attr.clear();
            attr.put("station", station);

            ResponseModel stationResponseModel = new ResponseModel();

            List<ResponseModel> addressListModel = new ArrayList<>();
            for (FirestationModel firestationModel : fireStationDao.reads(attr)) {
                attr.clear();
                attr.put("address", firestationModel.getAddress());

                List<ResponseModel> personsRsList = new ArrayList<>();
                for (PersonModel personModel : personDao.reads(attr)) {
                    attr.clear();
                    attr.put("firstname", personModel.getFirstName());
                    attr.put("lastname", personModel.getLastName());

                    MedicalRecordModel medicalRecordModel = medicalRecordDao.reads(attr).get(0);

                    ResponseModel personRs = new ResponseModel();
                    personRs.put("lastname", personModel.getLastName());
                    personRs.put("firstname", personModel.getFirstName());
                    personRs.put("age", calculatorService.caculateYearsOld(medicalRecordModel.getBirthdate()));
                    personRs.put("phone", personModel.getPhone());
                    personRs.put("allergies", medicalRecordModel.getAllergies());
                    personRs.put("medications", medicalRecordModel.getMedications());
                    personsRsList.add(personRs);
                }

                ResponseModel addressRsModel = new ResponseModel();
                addressRsModel.put("address", firestationModel.getAddress());
                addressRsModel.put("persons", personsRsList);
                addressListModel.add(addressRsModel);
            }
            stationResponseModel.put("station", station);
            stationResponseModel.put("address", addressListModel);
            responseList.add(stationResponseModel);
        }

        return ResponseEntity.ok(responseList);
    }
}
