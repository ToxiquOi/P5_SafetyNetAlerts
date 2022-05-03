package fr.ocr.p5_safetynetalerts.service;

import fr.ocr.p5_safetynetalerts.exception.DatabaseException;
import fr.ocr.p5_safetynetalerts.model.AbstractModel;
import org.springframework.stereotype.Service;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class ModelReflectionService {

    /**
     * Search a method in a class
     * @param name method searched
     * @param c class containing method
     * @return An Optional<Method> object
     */
    public Optional<Method> searchMethod(String name, Class<?> c) {
        return Arrays.stream(c.getMethods())
                .filter(mSearched -> mSearched.getName().equalsIgnoreCase(name))
                .findFirst();
    }

    public boolean isModelContainEntry(Class<?> c, AbstractModel model, Map.Entry<String, String> entry) throws DatabaseException {

        Optional<Method> m = searchMethod("get" + entry.getKey().toLowerCase(), c);
        if(m.isPresent())
            return isContainingValueInvoker(m.get(), model, entry.getValue());
        else
            return false;
    }

    /**
     * Search a method in a class
     * @param name method searched
     * @param c class containing method
     * @param args argument types of the seached method
     * @return An Optional<Method> object
     */
    public Optional<Method> searchMethod(String name, Class<?> c, Class<?>... args) {
        return Arrays.stream(c.getMethods())
                .filter(mSearched -> mSearched.getName().equalsIgnoreCase(name) && Arrays.equals(mSearched.getParameterTypes(), args))
                .findFirst();
    }

    /**
     * Method used to match a value stored in an AbstractModel,
     * this method is able to match a value in a String Type or a List
     * @param m Method to invoke
     * @param model AbstractModel inherited instance containing the method 'm'
     * @param valueToMatch The value we need to match in an object
     * @return true if the parameter isFinded is true and the value searched is Matched
     * @throws DatabaseException Invoking error
     */
    private boolean isContainingValueInvoker(Method m, AbstractModel model, String valueToMatch) throws DatabaseException {
        try {
            if(m.getReturnType().equals(List.class))
                return ((List<?>) m
                        .invoke(model))
                        .stream()
                        .anyMatch(s -> s.equals(valueToMatch));
            else
                return m.invoke(model)
                        .toString()
                        .contains(valueToMatch);
        } catch (Exception ex) {
            throw new DatabaseException(ex.getMessage());
        }
    }
}
