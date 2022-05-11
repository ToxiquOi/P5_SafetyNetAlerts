package fr.ocr.p5_safetynetalerts.dao;

import fr.ocr.p5_safetynetalerts.service.Database;
import fr.ocr.p5_safetynetalerts.exception.DatabaseException;
import fr.ocr.p5_safetynetalerts.exception.ElementNotFoundException;
import fr.ocr.p5_safetynetalerts.model.MedicalRecordModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class MedicalRecordDao extends AbstractDao<MedicalRecordModel> {

    @Autowired
    protected MedicalRecordDao(Database database) {
        super(database, MedicalRecordModel.class, "MedicalRecord");
    }

    public MedicalRecordModel deleteByFirstNameAndLastName(String firstName, String lastName) throws DatabaseException, ElementNotFoundException {
        Map<String, String> attributes = new HashMap<>();
        attributes.put("FirstName", firstName);
        attributes.put("LastName", lastName);


        List<MedicalRecordModel> personModelList = this.reads(attributes);

        if(personModelList.isEmpty()) {
            throw new ElementNotFoundException(lastName + " " + firstName + " doesn't exist");
        }

        MedicalRecordModel model = personModelList.get(0);
        return delete(model.getId());
    }
}
