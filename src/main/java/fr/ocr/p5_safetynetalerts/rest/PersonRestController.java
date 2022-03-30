package fr.ocr.p5_safetynetalerts.rest;

import fr.ocr.p5_safetynetalerts.dao.PersonDao;
import fr.ocr.p5_safetynetalerts.model.PersonModel;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("person")
public class PersonRestController extends AbstractRestExceptionHandler {

    private final PersonDao personDao;

    @Autowired
    public PersonRestController(PersonDao personDao) {
        this.personDao = personDao;
    }

    @SneakyThrows
    @PostMapping
    public ResponseEntity<PersonModel> addPerson(@RequestBody PersonModel personModel) {
        return ResponseEntity.ok(personDao.create(personModel));
    }

    @SneakyThrows
    @PutMapping("/{id}")
    public ResponseEntity<PersonModel> updatePerson(@RequestBody Map<String, Object> propertiesUpdate, @PathVariable int id) {
        PersonModel updatedModel = personDao.update(id, propertiesUpdate);

        return ResponseEntity.ok(updatedModel);
    }

    @SneakyThrows
    @DeleteMapping
    public ResponseEntity<Void> deletePerson(@RequestParam(name = "FirstName") String firstname,
                                             @RequestParam(name = "LastName") String lastname) {
        personDao.deleteByFirstNameAndLastName(firstname, lastname);
        return ResponseEntity.ok().build();
    }
}
