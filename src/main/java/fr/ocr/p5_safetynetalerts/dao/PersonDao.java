package fr.ocr.p5_safetynetalerts.dao;

import fr.ocr.p5_safetynetalerts.database.Database;
import fr.ocr.p5_safetynetalerts.model.PersonModel;
import org.springframework.stereotype.Repository;

@Repository
public class PersonDao extends AbstractDao<PersonModel> {
    protected PersonDao() {
        super(Database.getInstance(), PersonModel.class, "Person");
    }
}
