package fr.ocr.p5_safetynetalerts.controller.rest;

import fr.ocr.p5_safetynetalerts.dao.PersonDao;
import fr.ocr.p5_safetynetalerts.model.PersonModel;
import fr.ocr.p5_safetynetalerts.model.ResponseModel;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;
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
        checkIfNotNull(personModel);
        return ResponseEntity.ok(personDao.create(personModel));
    }

    @SneakyThrows
    @PutMapping("/{id}")
    public ResponseEntity<PersonModel> updatePerson(@NotNull @RequestBody Map<String, Object> propertiesUpdate, @NotNull @PathVariable int id) {
        checkIfNotNull(propertiesUpdate, id);
        return ResponseEntity.ok(personDao.update(id, propertiesUpdate));
    }

    @SneakyThrows
    @DeleteMapping
    public ResponseEntity<ResponseModel> deletePerson(@NotNull @RequestParam(name = "FirstName") String firstname,
                                                      @NotNull @RequestParam(name = "LastName") String lastname) {
        checkIfNotNull(firstname, lastname);
        personDao.deleteByFirstNameAndLastName(firstname, lastname);

        ResponseModel response = new ResponseModel();
        response.put("result", true);

        return ResponseEntity.ok(response);
    }
}
