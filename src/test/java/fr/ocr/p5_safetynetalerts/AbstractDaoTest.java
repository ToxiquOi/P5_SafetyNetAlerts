package fr.ocr.p5_safetynetalerts;

import fr.ocr.p5_safetynetalerts.component.AbstractDaoTestComponent;
import fr.ocr.p5_safetynetalerts.component.AbstractModelTestImp;
import fr.ocr.p5_safetynetalerts.service.Database;
import fr.ocr.p5_safetynetalerts.exception.DatabaseException;
import fr.ocr.p5_safetynetalerts.exception.ElementNotFoundException;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.HashMap;
import java.util.Map;

@SpringBootTest
class AbstractDaoTest {

    @Autowired
    private Database db;

    @Autowired
    private AbstractDaoTestComponent daoTestImp;

    @BeforeEach
    private void BeforePerTest() {
        db.initializeTable(AbstractModelTestImp.class);
    }

    @AfterEach
    private void AfterPerTest() {
        db.truncate();
    }

    @Test
    void testCreate() throws DatabaseException {
        Assertions.assertEquals(0, db.countElementInTable(AbstractModelTestImp.class));
        daoTestImp.create(new AbstractModelTestImp());
        Assertions.assertEquals(1, db.countElementInTable(AbstractModelTestImp.class));
    }

    @Test
    void testUpdate() throws DatabaseException, ElementNotFoundException {
        AbstractModelTestImp model = new AbstractModelTestImp();
        model.setValue("test");
        model = daoTestImp.create(model);

        Assertions.assertNotNull(daoTestImp.read(1));

        Map<String, Object> attributes = new HashMap<>();
        attributes.put("value", "update");

        Assertions.assertNotNull(daoTestImp.update(model.getId(), attributes));

        Assertions.assertEquals("update", daoTestImp.read(model.getId()).getValue());
    }

    @Test
    void testUpdateThrowElementNotFound() throws DatabaseException {
        AbstractModelTestImp model = new AbstractModelTestImp();
        model.setValue("test");
        daoTestImp.create(model);

        Map<String, Object> attributes = new HashMap<>();
        attributes.put("value", "update");

        Assertions.assertThrows(ElementNotFoundException.class, () -> daoTestImp.update(2, attributes));
    }

    @Test
    void testReadThrowElementNotFound() {
        Assertions.assertThrows(ElementNotFoundException.class, () -> daoTestImp.read(1));
    }

    @Test
    void testDeleteElement() throws DatabaseException, ElementNotFoundException {
        AbstractModelTestImp model = new AbstractModelTestImp();
        model.setValue("test");
        daoTestImp.create(model);

        Assertions.assertEquals(1, db.countElementInTable(AbstractModelTestImp.class));
        daoTestImp.delete(1);
        Assertions.assertEquals(0, db.countElementInTable(AbstractModelTestImp.class));
    }

    @Test
    void testDeleteThrowElementNotFound() {
        Assertions.assertThrows(ElementNotFoundException.class, () -> daoTestImp.delete(1));
    }

    @Test
    void testGetElement() throws DatabaseException, ElementNotFoundException {
        AbstractModelTestImp model = new AbstractModelTestImp();
        model.setValue("test");
        daoTestImp.create(model);

        Assertions.assertEquals(model, daoTestImp.reads(new HashMap<>()).get(0));
    }
}
