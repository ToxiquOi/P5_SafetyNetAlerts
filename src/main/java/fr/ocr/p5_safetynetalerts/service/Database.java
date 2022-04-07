package fr.ocr.p5_safetynetalerts.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import fr.ocr.p5_safetynetalerts.exception.DatabaseException;
import fr.ocr.p5_safetynetalerts.model.*;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

@Service
public class Database {

    private final Map<Class<? extends AbstractModel>, Integer> indexes = new HashMap<>();
    private final Map<Class<? extends AbstractModel>, List<AbstractModel>> data = new HashMap<>();


    private  Database() {
        loadData(getClass().getClassLoader().getResourceAsStream("data.json"));
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
            e.printStackTrace();
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
        final List<Throwable> throwables = new ArrayList<>();

        List<AbstractModel> result = data.get(c)
            .stream()
            .filter(model -> {
                boolean isFinded = true;
                for (Map.Entry<String, String> attr : attributes.entrySet()) {
                    try {
                        isFinded = invokeMatchingHandler(c, isFinded, model, attr);
                    } catch (DatabaseException e) {
                        throwables.add(e);
                    }
                }
                return isFinded;
            }).toList();

        // Handling error
        if (result.isEmpty() && !throwables.isEmpty()) {
            StringBuilder sb = new StringBuilder();
            throwables.forEach(ex -> {
                sb.append(ex.getLocalizedMessage());
                sb.append("\n");
            });

            throw new DatabaseException("Error during data processing, " + sb);
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
                Optional<Method> m = searchMethod("set" + StringUtils.capitalize(attr.getKey().toLowerCase()), c, String.class);
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

    /**
     * Search a method in a class
     * @param name method searched
     * @param c class containing method
     * @return An Optional<Method> object
     */
    private Optional<Method> searchMethod(String name, Class<?> c) {
        return Arrays.stream(c.getMethods())
                .filter(mSearched -> mSearched.getName().equalsIgnoreCase(name))
                .findFirst();
    }

    /**
     * Search a method in a class
     * @param name method searched
     * @param c class containing method
     * @param args argument types of the seached method
     * @return An Optional<Method> object
     */
    private Optional<Method> searchMethod(String name, Class<?> c, Class<?>... args) {
        return Arrays.stream(c.getMethods())
                .filter(mSearched -> mSearched.getName().equalsIgnoreCase(name) && Arrays.equals(mSearched.getParameterTypes(), args))
                .findFirst();
    }


    private boolean invokeMatchingHandler(Class<?> c, boolean isFinded, AbstractModel model, Map.Entry<String, String> entry) throws DatabaseException {

        Optional<Method> m = searchMethod("get" + entry.getKey().toLowerCase(), c);
        if(m.isPresent())
            isFinded = invokeMatchingMethod(m.get(), isFinded, model, entry.getValue());

        return isFinded;
    }

    /**
     * Method used to match a value stored in an AbstractModel,
     * this method is able to match a value in a String Type or a List
     * @param m Method to invoke
     * @param isFinded A flag setted after invoking method
     * @param model AbstractModel inherited instance containing the method 'm'
     * @param valueToMatch The value we need to match in an object
     * @return true if the parameter isFinded is true and the value searched is Matched
     * @throws DatabaseException Invoking error
     */
    private boolean invokeMatchingMethod(Method m, boolean isFinded, AbstractModel model, String valueToMatch) throws DatabaseException {
        try {
            if(m.getReturnType().equals(List.class))
                isFinded &= ((List<?>) m
                        .invoke(model))
                        .stream()
                        .anyMatch(s -> s.equals(valueToMatch));
            else
                isFinded &= m
                        .invoke(model)
                        .toString()
                        .contains(valueToMatch);

            return isFinded;
        } catch (Exception ex) {
            throw new DatabaseException(ex.getMessage());
        }
    }
}
