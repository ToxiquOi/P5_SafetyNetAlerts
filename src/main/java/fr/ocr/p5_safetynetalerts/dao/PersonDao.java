package fr.ocr.p5_safetynetalerts.dao;

import fr.ocr.p5_safetynetalerts.service.Database;
import fr.ocr.p5_safetynetalerts.exception.DatabaseException;
import fr.ocr.p5_safetynetalerts.exception.ElementNotFoundException;
import fr.ocr.p5_safetynetalerts.model.PersonModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class PersonDao extends AbstractDao<PersonModel> {

    @Autowired
    protected PersonDao(Database database) {
        super(database, PersonModel.class, "Person");
    }

    public PersonModel deleteByFirstNameAndLastName(String firstName, String lastName) throws DatabaseException, ElementNotFoundException {
        Map<String, String> attributes = new HashMap<>();
        attributes.put("FirstName", firstName);
        attributes.put("LastName", lastName);

        List<PersonModel> personModelList = this.reads(attributes);

        if(personModelList.isEmpty()) {
            throw new ElementNotFoundException(lastName + " " + firstName + " doesn't exist");
        }

        PersonModel model = personModelList.get(0);
        return delete(model.getId());
    }
}
