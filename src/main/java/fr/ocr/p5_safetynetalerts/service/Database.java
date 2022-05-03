package fr.ocr.p5_safetynetalerts.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import fr.ocr.p5_safetynetalerts.exception.DatabaseException;
import fr.ocr.p5_safetynetalerts.model.*;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

@Log4j2
@Service
public class Database {

    private final Map<Class<? extends AbstractModel>, Integer> indexes = new HashMap<>();
    private final Map<Class<? extends AbstractModel>, List<AbstractModel>> data = new HashMap<>();
    private final ModelReflectionService reflectionService;

    @Autowired
    private  Database(ModelReflectionService reflectionService) {
        loadData(getClass().getClassLoader().getResourceAsStream("data.json"));
        this.reflectionService = reflectionService;
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
            this.initializeTable(PersonModel.class);
            dataModel.persons.forEach(this::addElement);

            this.initializeTable(FirestationModel.class);
            dataModel.firestations.forEach(this::addElement);

            this.initializeTable(MedicalRecordModel.class);
            dataModel.medicalrecords.forEach(this::addElement);
        } catch (IOException e) {
            log.error(e.getLocalizedMessage());
        }
    }

    /**
     * Create an entry of type 'T' with value of 'obj' in database
     * @param obj object of type 'T' to add in database
     * @param <T> extends AbstractModel Type need to extend model
     */
    public <T extends AbstractModel> T addElement(T obj) {
        // Deported indexes handling
        int newIndexes = indexes.get(obj.getClass()) + 1;
        indexes.replace(obj.getClass(), newIndexes);

        obj.setId(newIndexes);
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

        List<AbstractModel> result = new ArrayList<>();
        for(AbstractModel model : data.get(c)) {
            boolean isFinded = true;
            for(Map.Entry<String, String> attr : attributes.entrySet()) {
                isFinded = reflectionService.isModelContainEntry(c, model, attr);
                if(!isFinded) break;
            }
            if(!isFinded) continue;

            result.add(model);
        }

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
    public <T extends AbstractModel> T updateElement(Class<T> c, int id, Map<String, Object> attributes) throws DatabaseException {
        if(!data.containsKey(c)) throw new DatabaseException("Table not exist");

        Optional<T> toUpdate = getElementById(c, id);

        if(toUpdate.isEmpty()) return null;

        T element = toUpdate.get();

        for (Map.Entry<String, Object> attr : attributes.entrySet()) {
            try {
                Optional<Method> m = reflectionService.searchMethod("set" + StringUtils.capitalize(attr.getKey().toLowerCase()), c, String.class);
                if(m.isPresent()) m.get().invoke(element, attr.getValue());

            } catch (InvocationTargetException | IllegalAccessException e) {
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
        return data.get(c).removeIf(m -> m.getId() == id);
    }

    public <T extends AbstractModel> int countElementInTable(Class<T> c) throws DatabaseException {
        if(!data.containsKey(c)) throw new DatabaseException("Table not exist");
        return data.get(c).size();
    }

    public <T extends AbstractModel> void initializeTable(Class<T> c) {
        if(!data.containsKey(c)) {
            indexes.put(c, 0);
            data.put(c, new ArrayList<>());
        }
    }

    public void truncate() {
        data.clear();
    }
}
