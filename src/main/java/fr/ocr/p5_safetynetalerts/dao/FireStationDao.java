package fr.ocr.p5_safetynetalerts.dao;

import fr.ocr.p5_safetynetalerts.database.Database;
import fr.ocr.p5_safetynetalerts.model.FirestationModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class FireStationDao extends AbstractDao<FirestationModel> {

    @Autowired
    protected FireStationDao(Database database) {
        super(database, FirestationModel.class, "Firestation");
    }
}
