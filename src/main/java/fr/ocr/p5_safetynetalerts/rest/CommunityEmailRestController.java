package fr.ocr.p5_safetynetalerts.rest;

import fr.ocr.p5_safetynetalerts.dao.PersonDao;
import fr.ocr.p5_safetynetalerts.model.PersonModel;
import fr.ocr.p5_safetynetalerts.model.ResponseModel;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("communityEmail")
public class CommunityEmailRestController extends AbstractRestExceptionHandler {
    private final PersonDao personDao;

    @Autowired
    public CommunityEmailRestController(PersonDao personDao) {
        this.personDao = personDao;
    }

    @SneakyThrows
   @GetMapping
    public ResponseEntity<ResponseModel> getPersonEMailFromCity(@RequestParam(name = "city") String cityName) {
        checkIfNotNull(cityName);
        Map<String, String> attributes = new HashMap<>();
        attributes.put("city", cityName);

        List<PersonModel> persons = personDao.reads(attributes);

        //Prepare response
        ResponseModel rsModel = new ResponseModel();

        // Fill contents ResponseModel
        persons.forEach(personModel ->
                rsModel.put(personModel.getLastName() + " " + personModel.getFirstName(), personModel.getPhone())
        );

        return ResponseEntity.ok(rsModel);
    }
}
