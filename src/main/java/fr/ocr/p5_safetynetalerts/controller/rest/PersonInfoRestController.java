package fr.ocr.p5_safetynetalerts.controller.rest;

import fr.ocr.p5_safetynetalerts.dao.MedicalRecordDao;
import fr.ocr.p5_safetynetalerts.dao.PersonDao;
import fr.ocr.p5_safetynetalerts.model.MedicalRecordModel;
import fr.ocr.p5_safetynetalerts.model.PersonModel;
import fr.ocr.p5_safetynetalerts.model.ResponseModel;
import fr.ocr.p5_safetynetalerts.service.CalculatorService;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("personInfo")
public class PersonInfoRestController extends AbstractRestExceptionHandler {

    private final MedicalRecordDao medicalRecordDao;
    private final PersonDao personDao;
    private final CalculatorService calculatorService;

    @Autowired
    public PersonInfoRestController(MedicalRecordDao medicalRecordDao, PersonDao personDao, CalculatorService calculatorService) {
        this.medicalRecordDao = medicalRecordDao;
        this.personDao = personDao;
        this.calculatorService = calculatorService;
    }

    @SneakyThrows
   @GetMapping
    public ResponseEntity<List<ResponseModel>> getMedicalRecordFromFirstNameAndLastName(@RequestParam(name = "FirstName") String firstname, @NotNull @RequestParam(name = "LastName") String lastname) {
        checkIfNotNull(lastname);
        Map<String, String> attributes = new HashMap<>();

        int indexMainPerson = 0;

        attributes.put("lastname", lastname);

        List<ResponseModel> personRsList = new ArrayList<>();
        for(PersonModel personModel : personDao.reads(attributes)) {
            if(attributes.containsKey("firstname"))
                attributes.replace("firstname", personModel.getFirstName());
            else
            attributes.put("firstname", personModel.getFirstName());

            MedicalRecordModel medicalRecordModel = medicalRecordDao.reads(attributes).get(0);


            int yearsOld = calculatorService.caculateYearsOld(medicalRecordModel.getBirthdate());

            //Prepare response contents
            ResponseModel personRs = new ResponseModel();
            personRs.put("firstname", personModel.getFirstName());
            personRs.put("lastname", personModel.getLastName());
            personRs.put("age", yearsOld);
            personRs.put("address", personModel.getAddress());
            personRs.put("mail", personModel.getEmail());
            personRs.put("allergies", medicalRecordModel.getAllergies());
            personRs.put("medications", medicalRecordModel.getMedications());

            personRsList.add(personRs);
            if(personModel.getFirstName().equals(firstname))
                indexMainPerson = personRsList.indexOf(personRs);
        }

        // Move the main searched Person to the first place of the list
        if(indexMainPerson != 0) {
            ResponseModel mainModelRequest = personRsList.remove(indexMainPerson);
            personRsList.add(0, mainModelRequest);
        }

        return ResponseEntity.ok(personRsList);
    }
}
