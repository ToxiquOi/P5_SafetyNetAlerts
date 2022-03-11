package fr.ocr.p5_safetynetalerts.rest;

import fr.ocr.p5_safetynetalerts.dao.PersonDao;
import fr.ocr.p5_safetynetalerts.exception.DatabaseException;
import fr.ocr.p5_safetynetalerts.exception.ElementNotFoundException;
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
        Map<String, String> attributes = new HashMap<>();
        attributes.put("city", cityName);

        List<PersonModel> persons = personDao.reads(attributes);

        //Prepare response
        ResponseModel rsModel = new ResponseModel();

        // Fill contents ResponseModel
        Map<String,String> rsContents = new HashMap<>();
        persons.forEach(personModel ->
                rsContents.put(personModel.getLastName() + " " + personModel.getFirstName(), personModel.getPhone())
        );

        rsModel.put(cityName, rsContents);
        return ResponseEntity.ok(rsModel);
    }
}
