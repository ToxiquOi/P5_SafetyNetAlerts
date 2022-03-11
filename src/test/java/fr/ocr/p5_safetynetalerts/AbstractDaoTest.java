package fr.ocr.p5_safetynetalerts;

import fr.ocr.p5_safetynetalerts.component.AbstractDaoTestComponent;
import fr.ocr.p5_safetynetalerts.component.AbstractModelTestImp;
import fr.ocr.p5_safetynetalerts.database.Database;
import fr.ocr.p5_safetynetalerts.exception.DatabaseException;
import fr.ocr.p5_safetynetalerts.exception.ElementNotFoundException;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.HashMap;
import java.util.Map;

@SpringBootTest
@TestClassOrder(ClassOrderer.OrderAnnotation.class)
public class AbstractDaoTest {

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
    void testUpdateThrowElementNotFound() {
        AbstractModelTestImp model = new AbstractModelTestImp();
        model.setValue("test");
        daoTestImp.create(model);

        Map<String, Object> attributes = new HashMap<>();
        attributes.put("value", "update");

        Assertions.assertThrows(ElementNotFoundException.class, () -> daoTestImp.update(2, attributes));
    }
}
