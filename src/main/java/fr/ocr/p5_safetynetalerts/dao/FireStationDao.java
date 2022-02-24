package fr.ocr.p5_safetynetalerts.dao;

import fr.ocr.p5_safetynetalerts.database.Database;
import fr.ocr.p5_safetynetalerts.model.FirestationModel;

public class FireStationDao extends AbstractDao<FirestationModel> {
    protected FireStationDao() {
        super(Database.getInstance(), FirestationModel.class, "Firestation");
    }
}
