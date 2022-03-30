package fr.ocr.p5_safetynetalerts.rest;

import fr.ocr.p5_safetynetalerts.dao.FireStationDao;
import fr.ocr.p5_safetynetalerts.dao.MedicalRecordDao;
import fr.ocr.p5_safetynetalerts.dao.PersonDao;
import fr.ocr.p5_safetynetalerts.model.FirestationModel;
import fr.ocr.p5_safetynetalerts.model.PersonModel;
import fr.ocr.p5_safetynetalerts.model.ResponseModel;
import fr.ocr.p5_safetynetalerts.utils.YearsOldCalculatorUtils;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("firestation")
public class FirestationRestController extends AbstractRestExceptionHandler {

    private final FireStationDao fireStationDao;
    private final PersonDao personDao;
    private final MedicalRecordDao medicalRecordDao;

    @Autowired
    public FirestationRestController(FireStationDao fireStationDao, PersonDao personDao, MedicalRecordDao medicalRecordDao) {
        this.fireStationDao = fireStationDao;
        this.personDao = personDao;
        this.medicalRecordDao = medicalRecordDao;
    }

    @SneakyThrows
    @PostMapping
    public ResponseEntity<FirestationModel> addFirestation(@RequestBody FirestationModel personModel) {
        return ResponseEntity.ok(fireStationDao.create(personModel));
    }

    @SneakyThrows
    @PutMapping("/{id}")
    public ResponseEntity<FirestationModel> updateFirestation(@RequestBody Map<String, Object> propertiesUpdate, @PathVariable int id) {
        FirestationModel updatedModel = fireStationDao.update(id, propertiesUpdate);

        return ResponseEntity.ok(updatedModel);
    }

    @SneakyThrows
    @DeleteMapping
    public ResponseEntity<ResponseModel> deleteFirestation() {
        //TODO: Supprime le mapping ?
        return ResponseEntity.ok(null);
    }

    @SneakyThrows
   @GetMapping
    public ResponseEntity<ResponseModel> getPersonCoveredByFirestation(@RequestParam String stationNumber) {
        Map<String, String> attr = new HashMap<>();
        attr.put("station", stationNumber);

        ResponseModel rsModel = new ResponseModel();
        int nbAdult = 0;
        int nbChild = 0;


        for (FirestationModel station : fireStationDao.reads(attr)) {
            attr.clear();
            attr.put("address", station.getAddress());

            List<Map<String, String>> persons = new ArrayList<>();
            for (PersonModel personModel : personDao.reads(attr)) {
                Map<String, String> person = new TreeMap<>();
                person.put("lastname", personModel.getLastName());
                person.put("firstname", personModel.getFirstName());

                if (18 > YearsOldCalculatorUtils.caculateYearsOld(medicalRecordDao.reads(person).get(0).getBirthdate()))
                    nbChild++;
                else
                    nbAdult++;

                person.put("phone", personModel.getPhone());
                persons.add(person);
            }

            rsModel.put(station.getAddress(), persons);
        }

        rsModel.put("childs", nbChild);
        rsModel.put("Adults", nbAdult);

        return ResponseEntity.ok(rsModel);
    }
}
