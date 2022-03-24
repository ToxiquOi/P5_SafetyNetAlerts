package fr.ocr.p5_safetynetalerts.rest;

import fr.ocr.p5_safetynetalerts.dao.FireStationDao;
import fr.ocr.p5_safetynetalerts.dao.MedicalRecordDao;
import fr.ocr.p5_safetynetalerts.dao.PersonDao;
import fr.ocr.p5_safetynetalerts.model.FirestationModel;
import fr.ocr.p5_safetynetalerts.model.MedicalRecordModel;
import fr.ocr.p5_safetynetalerts.model.PersonModel;
import fr.ocr.p5_safetynetalerts.model.ResponseModel;
import fr.ocr.p5_safetynetalerts.utils.YearsOldCalculatorUtils;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.*;

@RestController
@RequestMapping("flood")
public class FloodRestController extends AbstractRestExceptionHandler {

    private final FireStationDao fireStationDao;
    private final PersonDao personDao;
    private final MedicalRecordDao medicalRecordDao;

    @Autowired
    public FloodRestController(FireStationDao fireStationDao, PersonDao personDao, MedicalRecordDao medicalRecordDao) {
        this.fireStationDao = fireStationDao;
        this.personDao = personDao;
        this.medicalRecordDao = medicalRecordDao;
    }

    @SneakyThrows
    @GetMapping("/stations")
    public ResponseEntity<ResponseModel> getHomeDependingFromFireStation(@RequestParam List<String> stations) {

        ResponseModel rsModel = new ResponseModel();

        Map<String, String> attr = new HashMap<>();

        for (String station : stations) {
            attr.clear();
            attr.put("station", station);

            Map<String, List<ResponseModel>> address = new TreeMap<>();
            for (FirestationModel firestationModel : fireStationDao.reads(attr)) {
                attr.clear();
                attr.put("address", firestationModel.getAddress());

                List<ResponseModel> persons = new ArrayList<>();
                for (PersonModel personModel : personDao.reads(attr)) {
                    attr.clear();
                    attr.put("firstname", personModel.getFirstName());
                    attr.put("lastname", personModel.getLastName());

                    MedicalRecordModel medicalRecordModel = medicalRecordDao.reads(attr).get(0);

                    ResponseModel personRs = new ResponseModel();
                    personRs.put("lastname", personModel.getLastName());
                    personRs.put("firstname", personModel.getFirstName());
                    personRs.put("age", YearsOldCalculatorUtils.caculateYearsOld(medicalRecordModel.getBirthdate()));
                    personRs.put("phone", personModel.getPhone());
                    personRs.put("allergies", medicalRecordModel.getAllergies());
                    personRs.put("medications", medicalRecordModel.getMedications());

                    persons.add(personRs);
                }

                address.put(firestationModel.getAddress(), persons);
            }
            rsModel.put(station, address);
        }

        return ResponseEntity.ok(rsModel);
    }
}
