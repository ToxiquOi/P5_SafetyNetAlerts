package fr.ocr.p5_safetynetalerts.database;

import com.fasterxml.jackson.databind.ObjectMapper;
import fr.ocr.p5_safetynetalerts.exception.DatabaseException;
import fr.ocr.p5_safetynetalerts.model.AbstractModel;
import fr.ocr.p5_safetynetalerts.model.FirestationModel;
import fr.ocr.p5_safetynetalerts.model.MedicalRecordModel;
import fr.ocr.p5_safetynetalerts.model.PersonModel;
import org.springframework.util.StringUtils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;


public class Database {

    private static Database database;

    private final Map<Class<? extends AbstractModel>, List<AbstractModel>> data = new HashMap<>();

    private  Database() {
    }

    public static Database getInstance() {
        if(database == null) database = new Database();
        return database;
    }

    /**
     * Initialise data
     * @param is Input stream pointing on a valid Json file
     */
    public void loadData(InputStream is) {
        data.clear();

        ObjectMapper mapper = new ObjectMapper();
        try {
            ImportDataModel dataModel = mapper.readValue(is, ImportDataModel.class);
            dataModel.persons.forEach(this::addElement);
            dataModel.firestations.forEach(this::addElement);
            dataModel.medicalrecords.forEach(this::addElement);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Create an entry of type 'T' with value of 'obj' in database
     * @param obj object of type 'T' to add in database
     * @param <T> extends AbstractModel Type need to extend model
     */
    public <T extends AbstractModel> T addElement(T obj) {
        if(!data.containsKey(obj.getClass())) data.put(obj.getClass(), new ArrayList<>());
        obj.setId(data.get(obj.getClass()).size() + 1);
        data.get(obj.getClass()).add(obj);
        return obj;
    }

    /**
     * Get one element from database with id
     * @param c class is used like a table identifier
     * @param id id of the requested item
     * @param <T> type of the requested item (T == c)
     * @return Return element from table 'c' containing 'id'
     * @throws DatabaseException thrown when DB table not exist
     */
    public <T extends AbstractModel> Optional<T> getElementById(Class<T> c, int id) throws DatabaseException {
        if(!data.containsKey(c)) throw new DatabaseException("Table not exist");
        return (Optional<T>) data.get(c).stream().filter(model -> model.getId() == id).findFirst();
    }

    /**
     * Get all element matching with all attributes
     * @param c class is used like a table identifier
     * @param attributes search critter's
     * @param <T> type of the requested item (T == c)
     * @return A list containing all elements matching attributes, empty list if no match
     * @throws DatabaseException thrown when DB table not exist
     */
    public <T extends AbstractModel> List<T> getElement(Class<T> c, Map<String, String> attributes) throws DatabaseException {
        if(!data.containsKey(c)) throw new DatabaseException("Table not exist");
        List<AbstractModel> result = data.get(c)
            .stream()
            .filter(model -> {
                boolean isFinded = false;
                for (Map.Entry<String, String> attr : attributes.entrySet()) {
                    try {
                        Method m = c.getMethod("get" + StringUtils.capitalize(attr.getKey()));
                        isFinded = m.invoke(model).toString().contains(attr.getValue());
                    } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
                        // TODO: Need refactor to better handling of exceptions ?
                        e.printStackTrace();
                    }
                }
                return isFinded;
            }).toList();


        return (List<T>) result;
    }

    /**
     * Update an element
     * @param c class is used like a table identifier
     * @param id id id of the requested item
     * @param attributes A map containing attributes to update, the key need to be sames as the name of field updated
     * @param <T> <T> type of the requested item (T == c)
     * @return Updated element
     * @throws DatabaseException if DB table not exist or if an error occured during the field update
     */
    public <T extends AbstractModel> T updateElement(Class<T> c, int id, Map<String, String> attributes) throws DatabaseException {
        if(!data.containsKey(c)) throw new DatabaseException("Table not exist");

        Optional<T> toUpdate = getElementById(c, id);

        if(toUpdate.isEmpty()) return null;

        T element = toUpdate.get();
        for (Map.Entry<String, String> attr : attributes.entrySet()) {
            try {
                Method m = c.getMethod("set" + StringUtils.capitalize(attr.getKey()), String.class);
                m.invoke(element, attr.getValue());
            } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
                throw new DatabaseException("Error during update", e);
            }
        }

        return element;
    }

    /**
     * Delete an element using is id
     * @param c class is used like a table identifier
     * @param id id of the requested element
     * @param <T> <T> type of the requested item (T == c)
     * @return true if element was supressed, false if the element not exist
     * @throws DatabaseException if DB table not exist
     */
    public <T extends AbstractModel> boolean deleteElementById(Class<T> c, int id) throws DatabaseException {
        if(!data.containsKey(c)) throw new DatabaseException("Table not exist");
        boolean result = false;

        Optional<T> toDelete = getElementById(c, id);

        if(toDelete.isPresent()) {
            data.get(c).remove(toDelete.get());
            result = true;
        }

        return result;
    }

    public <T extends AbstractModel> int countElementInTable(Class<T> c) {
        return data.get(c).size();
    }

    public void truncate() {
        data.clear();
    }
}
