package fr.ocr.p5_safetynetalerts.dao;

import fr.ocr.p5_safetynetalerts.database.Database;
import fr.ocr.p5_safetynetalerts.exception.DatabaseException;
import fr.ocr.p5_safetynetalerts.model.PersonModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;

@Repository
public class PersonDao extends AbstractDao<PersonModel> {

    @Autowired
    protected PersonDao(Database database) {
        super(database, PersonModel.class, "Person");
    }

    public boolean deleteByFirstNameAndLastName(String firstName, String lastName) throws DatabaseException {
        Map<String, String> attributes = new HashMap<>();
        attributes.put("FirstName", firstName);
        attributes.put("LastName", lastName);

        PersonModel model = this.reads(attributes).get(0);

        return delete(model.getId());
    }
}
