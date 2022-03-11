package fr.ocr.p5_safetynetalerts.rest;

import fr.ocr.p5_safetynetalerts.dao.FireStationDao;
import fr.ocr.p5_safetynetalerts.exception.DatabaseException;
import fr.ocr.p5_safetynetalerts.exception.ElementNotFoundException;
import fr.ocr.p5_safetynetalerts.model.FirestationModel;
import fr.ocr.p5_safetynetalerts.model.ResponseModel;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("firestation")
public class FirestationRestController {

    private final FireStationDao fireStationDao;

    @Autowired
    public FirestationRestController(FireStationDao fireStationDao) {
        this.fireStationDao = fireStationDao;
    }

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

    @GetMapping()
    public ResponseEntity<ResponseModel> getPersonCoveredByFirestation(@RequestParam String stationNumber) {
        /* TODO
            FireStation: number -> address
            Person: address -> nom, prenom, tel
            Stats: nbAdulte, nbEnfant
         */

        return ResponseEntity.ok(null);
    }

    @ExceptionHandler(ElementNotFoundException.class)
    public ResponseEntity<Void> handleElementNotFound(ElementNotFoundException ex) {
        return ResponseEntity.notFound().build();
    }

    @ExceptionHandler(DatabaseException.class)
    public ResponseEntity<Void> handleDatabaseException(DatabaseException ex) {
        return ResponseEntity.unprocessableEntity().build();
    }
}
