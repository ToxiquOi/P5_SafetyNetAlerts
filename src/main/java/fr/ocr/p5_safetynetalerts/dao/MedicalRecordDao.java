package fr.ocr.p5_safetynetalerts.dao;

import fr.ocr.p5_safetynetalerts.database.Database;
import fr.ocr.p5_safetynetalerts.model.MedicalRecordModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

@Repository
public class MedicalRecordDao extends AbstractDao<MedicalRecordModel> {

    @Autowired
    protected MedicalRecordDao(Database database) {
        super(database, MedicalRecordModel.class, "MedicalRecord");
    }
}
