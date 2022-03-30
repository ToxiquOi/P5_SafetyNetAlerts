package fr.ocr.p5_safetynetalerts.rest;

import fr.ocr.p5_safetynetalerts.dao.MedicalRecordDao;
import fr.ocr.p5_safetynetalerts.dao.PersonDao;
import fr.ocr.p5_safetynetalerts.model.MedicalRecordModel;
import fr.ocr.p5_safetynetalerts.model.PersonModel;
import fr.ocr.p5_safetynetalerts.model.ResponseModel;
import fr.ocr.p5_safetynetalerts.utils.YearsOldCalculatorUtils;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("personInfo")
public class PersonInfoRestController extends AbstractRestExceptionHandler {

    private final MedicalRecordDao medicalRecordDao;
    private final PersonDao personDao;

    @Autowired
    public PersonInfoRestController(MedicalRecordDao medicalRecordDao, PersonDao personDao) {
        this.medicalRecordDao = medicalRecordDao;
        this.personDao = personDao;
    }

    @SneakyThrows
   @GetMapping
    public ResponseEntity<ResponseModel> getMedicalRecordFromFirstNameAndLastName(@NotNull @RequestParam(name = "FirstName") String firstname,
                                                                                  @NotNull @RequestParam(name = "LastName") String lastname) {
        checkIfNotNull(firstname, lastname);
        Map<String, String> attributes = new HashMap<>();
        attributes.put("firstname", firstname);
        attributes.put("lastname", lastname);

        PersonModel personModel = personDao.reads(attributes).get(0);
        MedicalRecordModel medicalRecordModel = medicalRecordDao.reads(attributes).get(0);

        ResponseModel rsModel = new ResponseModel();
        String key = medicalRecordModel.getFirstName() + " " + medicalRecordModel.getLastName();

        //Calcul age
        int yearsOld = YearsOldCalculatorUtils.caculateYearsOld(medicalRecordModel.getBirthdate());

        //Prepare response contents
        Map<String, Object> value = new HashMap<>();
        value.put("age", yearsOld);
        value.put("address", personModel.getAddress());
        value.put("mail", personModel.getEmail());
        value.put("allergies", medicalRecordModel.getAllergies());
        value.put("medications", medicalRecordModel.getMedications());

        rsModel.put(key, value);

        return ResponseEntity.ok(rsModel);
    }
}
