package fr.ocr.p5_safetynetalerts.controller.rest;

import fr.ocr.p5_safetynetalerts.dao.MedicalRecordDao;
import fr.ocr.p5_safetynetalerts.model.MedicalRecordModel;
import fr.ocr.p5_safetynetalerts.model.ResponseModel;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("medicalRecord")
public class MedicalRecordRestController extends AbstractRestExceptionHandler {

    private final MedicalRecordDao medicalRecordDao;

    @Autowired
    public MedicalRecordRestController(MedicalRecordDao medicalRecordDao) {
        this.medicalRecordDao = medicalRecordDao;
    }

    @SneakyThrows
    @PostMapping
    public ResponseEntity<MedicalRecordModel> addMedicalRecord(@RequestBody MedicalRecordModel personModel) {
        checkIfNotNull(personModel);
        return ResponseEntity.ok(medicalRecordDao.create(personModel));
    }

    @SneakyThrows
    @PutMapping("/{id}")
    public ResponseEntity<MedicalRecordModel> updateMedicalRecord(@RequestBody Map<String, Object> propertiesUpdate, @PathVariable int id) {
        checkIfNotNull(propertiesUpdate, id);
        return ResponseEntity.ok(medicalRecordDao.update(id, propertiesUpdate));
    }

    @SneakyThrows
    @DeleteMapping
    public ResponseEntity<MedicalRecordModel> deleteMedicalRecord(@RequestParam(name = "FirstName") String firstname,
                                                    @RequestParam(name = "LastName") String lastname) {
        checkIfNotNull(firstname, lastname);
        return ResponseEntity.ok(medicalRecordDao.deleteByFirstNameAndLastName(firstname, lastname));
    }
}
