package fr.ocr.p5_safetynetalerts;

import fr.ocr.p5_safetynetalerts.database.Database;
import fr.ocr.p5_safetynetalerts.exception.DatabaseException;
import fr.ocr.p5_safetynetalerts.model.FirestationModel;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class DatabaseTest {

    private Database db;

    @BeforeEach
    private void setUpPerTest() {
        db = Database.getInstance();
        db.loadData(getClass().getClassLoader().getResourceAsStream("data.json"));
    }

    @Test
    public void testCountElement() {
        Assertions.assertEquals(13, db.countElementInTable(FirestationModel.class));
    }

    @Test
    public void testAddElement() {
        FirestationModel fm = new FirestationModel("test", "2");
        db.addElement(fm);

        Assertions.assertEquals(14, db.countElementInTable(FirestationModel.class));
    }

    @Test
    public void testGetElementById() throws DatabaseException {
        Optional<FirestationModel> rs = db.getElementById(FirestationModel.class, 1);

        Assertions.assertTrue(rs.isPresent());
    }

    @Test
    public void testGetElementOneAttribute() throws DatabaseException {
        FirestationModel fm = new FirestationModel("test", "2");
        db.addElement(fm);
        db.addElement(fm);

        Map<String, String> attributes = new HashMap<>();
        attributes.put("address", fm.getAddress());

        List<FirestationModel> rs = db.getElement(FirestationModel.class, attributes);

        Assertions.assertEquals(2, rs.size());
    }

    @Test
    public void testGetElementMultipleAttribute() throws DatabaseException {
        FirestationModel fm = new FirestationModel("test", "666");
        db.addElement(fm);


        Map<String, String> attributes = new HashMap<>();
        attributes.put("address", fm.getAddress());
        attributes.put("station", fm.getStation());

        List<FirestationModel> rs = db.getElement(FirestationModel.class, attributes);
        Assertions.assertEquals(1, rs.size());

        db.addElement(fm);
        rs = db.getElement(FirestationModel.class, attributes);
        Assertions.assertEquals(2, rs.size());
    }

    @Test
    public void testDeleteElementById() throws DatabaseException {
        db.deleteElementById(FirestationModel.class, 2);
        Assertions.assertTrue(db.getElementById(FirestationModel.class, 2).isEmpty());
    }

    @Test
    public void testUpdateElementMultipleAttribute() throws DatabaseException {
        FirestationModel fm = new FirestationModel("test", "666");
        db.addElement(fm);


        Map<String, String> attributes = new HashMap<>();
        attributes.put("address", "test2");
        attributes.put("station", "666");

        db.updateElement(FirestationModel.class, fm.getId(), attributes);

        Optional<FirestationModel> updated = db.getElementById(FirestationModel.class, fm.getId());

        Assertions.assertEquals("666", updated.get().getStation());
        Assertions.assertEquals("test2", updated.get().getAddress());
    }
}
