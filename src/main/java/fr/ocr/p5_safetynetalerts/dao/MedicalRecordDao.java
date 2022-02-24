package fr.ocr.p5_safetynetalerts.dao;

import fr.ocr.p5_safetynetalerts.database.Database;
import fr.ocr.p5_safetynetalerts.model.MedicalRecordModel;

public class MedicalRecordDao extends AbstractDao<MedicalRecordModel> {
    protected MedicalRecordDao() {
        super(Database.getInstance(), MedicalRecordModel.class, "MedicalRecord");
    }
}
