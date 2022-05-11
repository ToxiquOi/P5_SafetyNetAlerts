package fr.ocr.p5_safetynetalerts.dao;

import fr.ocr.p5_safetynetalerts.service.Database;
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

    public T create(T obj) throws DatabaseException {
        if(obj == null)
            throw new DatabaseException("new element cannot be null");
        return database.addElement(obj);
    }

    public T read(int id) throws DatabaseException, ElementNotFoundException {
        Optional<T> opt = database.getElementById(cModel, id);
        if (opt.isEmpty())
            throw new ElementNotFoundException(StringUtils.capitalize(entityName) + "with id: " + id + ", not found in database");
        return opt.get();
    }

    public List<T> reads(Map<String, String> attributes) throws DatabaseException, ElementNotFoundException {
        List<T> list = database.getElement(cModel, attributes);
        if (list.isEmpty()) throw new ElementNotFoundException("No result in database for this request");
        return list;
    }

    public T update(int id, Map<String, Object> attributes) throws DatabaseException, ElementNotFoundException {
        T result = database.updateElement(cModel, id, attributes);
        if (result == null)
            throw new ElementNotFoundException(StringUtils.capitalize(entityName) + "with id: " + id + ", not found in database");
        return result;
    }

    public T delete(int id) throws DatabaseException, ElementNotFoundException {
        Optional<T> result = database.deleteElementById(cModel, id);
        if(result.isEmpty())
            throw new ElementNotFoundException(StringUtils.capitalize(entityName) + "with id: " + id + ", not found in database");
        return result.get();
    }
}
