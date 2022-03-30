package fr.ocr.p5_safetynetalerts.dao;

import fr.ocr.p5_safetynetalerts.database.Database;
import fr.ocr.p5_safetynetalerts.exception.DatabaseException;
import fr.ocr.p5_safetynetalerts.exception.ElementNotFoundException;
import fr.ocr.p5_safetynetalerts.model.FirestationModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Repository
public class FireStationDao extends AbstractDao<FirestationModel> {

    @Autowired
    protected FireStationDao(Database database) {
        super(database, FirestationModel.class, "Firestation");
    }

    public void suppressMapping(String station, String address) throws DatabaseException, ElementNotFoundException {
        Map<String, String> attributes = new HashMap<>();
        attributes.put("station", station);
        attributes.put("address", address);

        List<FirestationModel> firestationModels = this.reads(attributes);

        if(firestationModels.isEmpty()) {
            throw new ElementNotFoundException("mapping station: " + station + " address: " + address + " doesn't exist");
        }

        FirestationModel model = firestationModels.get(0);
        delete(model.getId());
    }
}
