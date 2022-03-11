package fr.ocr.p5_safetynetalerts.rest;

import fr.ocr.p5_safetynetalerts.dao.MedicalRecordDao;
import fr.ocr.p5_safetynetalerts.exception.DatabaseException;
import fr.ocr.p5_safetynetalerts.exception.ElementNotFoundException;
import fr.ocr.p5_safetynetalerts.model.MedicalRecordModel;
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

    @PostMapping
    public ResponseEntity<MedicalRecordModel> addMedicalRecord(@RequestBody MedicalRecordModel personModel) {
        return ResponseEntity.ok(medicalRecordDao.create(personModel));
    }

    @SneakyThrows
    @PutMapping("/{id}")
    public ResponseEntity<MedicalRecordModel> updateMedicalRecord(@RequestBody Map<String, Object> propertiesUpdate, @PathVariable int id) {
        MedicalRecordModel updatedModel = medicalRecordDao.update(id, propertiesUpdate);

        return ResponseEntity.ok(updatedModel);
    }

    @SneakyThrows
    @DeleteMapping
    public ResponseEntity<Void> deleteMedicalRecord(@RequestParam(name = "FirstName") String firstname,
                                             @RequestParam(name = "LastName") String lastname) {
        medicalRecordDao.deleteByFirstNameAndLastName(firstname, lastname);
        return ResponseEntity.ok().build();
    }
}
