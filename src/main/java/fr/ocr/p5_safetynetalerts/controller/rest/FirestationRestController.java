package fr.ocr.p5_safetynetalerts.controller.rest;

import fr.ocr.p5_safetynetalerts.dao.FireStationDao;
import fr.ocr.p5_safetynetalerts.dao.MedicalRecordDao;
import fr.ocr.p5_safetynetalerts.dao.PersonDao;
import fr.ocr.p5_safetynetalerts.model.FirestationModel;
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
@RequestMapping("firestation")
public class FirestationRestController extends AbstractRestExceptionHandler {

    private final FireStationDao fireStationDao;
    private final PersonDao personDao;
    private final MedicalRecordDao medicalRecordDao;
    private final CalculatorService calculatorService;

    @Autowired
    public FirestationRestController(FireStationDao fireStationDao,
                                     PersonDao personDao,
                                     MedicalRecordDao medicalRecordDao,
                                     CalculatorService calculatorService) {
        this.fireStationDao = fireStationDao;
        this.personDao = personDao;
        this.medicalRecordDao = medicalRecordDao;
        this.calculatorService = calculatorService;
    }


    @SneakyThrows
    @PostMapping
    public ResponseEntity<FirestationModel> addFirestation(@NotNull @RequestBody FirestationModel personModel) {
        return ResponseEntity.ok(fireStationDao.create(personModel));
    }

    @SneakyThrows
    @PutMapping("/{id}")
    public ResponseEntity<FirestationModel> updateFirestation(@RequestBody Map<String, Object> propertiesUpdate, @NotNull @PathVariable int id) {
        return ResponseEntity.ok(fireStationDao.update(id, propertiesUpdate));
    }

    @SneakyThrows
    @DeleteMapping
    public ResponseEntity<ResponseModel> deleteFirestationMapping(@NotNull @RequestParam String station,
                                                            @NotNull @RequestParam String address) {
        fireStationDao.suppressMapping(station, address);

        ResponseModel response = new ResponseModel();
        response.put("result", true);

        return ResponseEntity.ok(response);
    }

   @SneakyThrows
   @GetMapping
    public ResponseEntity<ResponseModel> getPersonCoveredByFirestation(@NotNull @RequestParam String stationNumber) {
        checkIfNotNull(stationNumber);
        Map<String, String> attr = new HashMap<>();
        attr.put("station", stationNumber);

        ResponseModel rsModel = new ResponseModel();
        int nbAdult = 0;
        int nbChild = 0;


        FirestationModel station = fireStationDao.reads(attr).get(0);
        attr.clear();
        attr.put("address", station.getAddress());

        List<Map<String, String>> persons = new ArrayList<>();
        for (PersonModel personModel : personDao.reads(attr)) {
            Map<String, String> person = new TreeMap<>();
            person.put("lastname", personModel.getLastName());
            person.put("firstname", personModel.getFirstName());

            if (18 <= calculatorService.caculateYearsOld(medicalRecordDao.reads(person).get(0).getBirthdate()))
                nbChild++;
            else
                nbAdult++;

            person.put("phone", personModel.getPhone());
            persons.add(person);
        }

        rsModel.put("persons", persons);
        rsModel.put("address", station.getAddress());
        rsModel.put("childs", nbChild);
        rsModel.put("adults", nbAdult);

        return ResponseEntity.ok(rsModel);
    }
}
