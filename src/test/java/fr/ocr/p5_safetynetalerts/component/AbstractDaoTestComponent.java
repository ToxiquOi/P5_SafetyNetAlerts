package fr.ocr.p5_safetynetalerts.component;

import fr.ocr.p5_safetynetalerts.dao.AbstractDao;
import fr.ocr.p5_safetynetalerts.database.Database;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class AbstractDaoTestComponent extends AbstractDao<AbstractModelTestImp> {

    @Autowired
    protected AbstractDaoTestComponent(Database database) {
        super(database, AbstractModelTestImp.class, "AbstractModel");
    }
}
