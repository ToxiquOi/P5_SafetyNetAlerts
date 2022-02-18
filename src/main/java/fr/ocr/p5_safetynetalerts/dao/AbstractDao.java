package fr.ocr.p5_safetynetalerts.dao;

import fr.ocr.p5_safetynetalerts.database.Database;
import fr.ocr.p5_safetynetalerts.exception.DatabaseException;
import fr.ocr.p5_safetynetalerts.exception.ElementNotFoundException;
import fr.ocr.p5_safetynetalerts.model.AbstractModel;
import org.springframework.util.StringUtils;


import java.util.List;
import java.util.Map;
import java.util.Optional;


public abstract class AbstractDao<T extends AbstractModel> {

    protected Database database;
    protected Class<T> cModel;
    protected String entityName;


    protected AbstractDao(Database database, Class<T> cModel, String entityName) {
        this.database = database;
        this.cModel = cModel;
        this.entityName = entityName;
    }

    protected T create(T obj) {
        return database.addElement(obj);
    }

    protected T read(int id) throws DatabaseException, ElementNotFoundException {
        Optional<T> opt = database.getElementById(cModel, id);
        if (opt.isEmpty())
            throw new ElementNotFoundException(StringUtils.capitalize(entityName) + "with id: " + id + ", not found in database");
        return opt.get();
    }

    protected List<T> reads(Map<String, String> attributes) throws DatabaseException {
        return database.getElement(cModel, attributes);
    }

    protected T update(int id, Map<String, String> attributes) throws DatabaseException, ElementNotFoundException {
        T result = database.updateElement(cModel, id, attributes);
        if (result == null)
            throw new ElementNotFoundException(StringUtils.capitalize(entityName) + "with id: " + id + ", not found in database");
        return result;
    }

    protected boolean delete(int id) throws DatabaseException {
        return database.deleteElementById(cModel, id);
    }
}
