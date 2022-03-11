package fr.ocr.p5_safetynetalerts;

import fr.ocr.p5_safetynetalerts.database.Database;
import fr.ocr.p5_safetynetalerts.exception.DatabaseException;
import fr.ocr.p5_safetynetalerts.model.FirestationModel;
import fr.ocr.p5_safetynetalerts.model.MedicalRecordModel;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@SpringBootTest
public class DatabaseTest {

    @Autowired
    private Database db;

    @BeforeEach
    private void BeforePerTest() {
        db.loadData(getClass().getClassLoader().getResourceAsStream("data.json"));
    }

    @AfterEach
    private void AfterPerTest() {
        db.truncate();
    }

    @Test
    public void testCountElement() throws DatabaseException {
        Assertions.assertEquals(13, db.countElementInTable(FirestationModel.class));
    }

    @Test
    public void testAddElement() throws DatabaseException {
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
        Map<String, String> attributes = new HashMap<>();
        attributes.put("allergies", "nillacilan");

        List<MedicalRecordModel> rs = db.getElement(MedicalRecordModel.class, attributes);

        Assertions.assertEquals(3, rs.size());
    }

    @Test
    public void testGetElementMultipleAttribute() throws DatabaseException {
        Map<String, String> attributes = new HashMap<>();
        attributes.put("firstName", "Ron");
        attributes.put("lastName", "Peters");

        List<MedicalRecordModel> rs = db.getElement(MedicalRecordModel.class, attributes);
        Assertions.assertEquals(1, rs.size());
    }

    @Test
    public void testDeleteElementById() throws DatabaseException {
        int before = db.countElementInTable(FirestationModel.class);
        db.deleteElementById(FirestationModel.class, 2);
        int after = db.countElementInTable(FirestationModel.class);

        Assertions.assertEquals(before - 1, after);
    }

    @Test
    public void testUpdateElementMultipleAttribute() throws DatabaseException {
        FirestationModel fm = new FirestationModel("test", "666");
        db.addElement(fm);


        Map<String, Object> attributes = new HashMap<>();
        attributes.put("address", "test2");
        attributes.put("station", "666");

        db.updateElement(FirestationModel.class, fm.getId(), attributes);

        Optional<FirestationModel> updated = db.getElementById(FirestationModel.class, fm.getId());

        Assertions.assertTrue(updated.isPresent());
        Assertions.assertEquals("666", updated.get().getStation());
        Assertions.assertEquals("test2", updated.get().getAddress());
    }
}
