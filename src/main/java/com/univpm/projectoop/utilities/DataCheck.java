package com.univpm.projectoop.utilities;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.regex.Pattern;

import static java.lang.Float.parseFloat;

/**
 *
 */
public class DataCheck {

    /**
     *
     */
    private int yearCondition;

    /**
     *
     */
    private Float [] valueFloat;

    /**
     *
     */
    private String [] valueString;

    /**
     *
     * @param fieldName
     * @return
     */
    public boolean isYear (String fieldName){

        boolean isYear = false;
        try{
            yearCondition = Integer.parseInt(fieldName);
            isYear = true;
        } catch (NumberFormatException e){
            e.getMessage();
            isYear = false;
        }
        return isYear;
    }

    /**
     *
     * @param strValue
     * @return
     */
    public boolean isArray (String strValue) {

        Object[] splittedValues;

        if (strValue.contains("[")) {
            String copy = strValue.substring(1, strValue.length() - 1);
            splittedValues = copy.split(",");
        } else {
            splittedValues = new Object[1];
            splittedValues[0] = strValue;
        }

        if (Pattern.matches("([0-9]*[.])?[0-9]+", splittedValues[0].toString())) {

            Float[] valueFloatloc = new Float[splittedValues.length];
            try {
                for (int i = 0; i < splittedValues.length; i++) {
                    valueFloatloc[i] = parseFloat(splittedValues[i].toString());
                }
                valueFloat = valueFloatloc;
                return true;
            } catch (Exception e) {
                e.printStackTrace();
            }

        } else if (Pattern.matches("[\"]?[a-zA-Z]+[0-9]*[\"]?", splittedValues[0].toString())) {

            String[] valueStringloc = new String[splittedValues.length];
            int lenght = splittedValues.length;
            try {

                if (lenght == 1){
                    valueStringloc[0] = splittedValues[0].toString();
                } else {
                    for (int i = 0; i < lenght; i++) {
                        valueStringloc[i] = splittedValues[i].toString().substring(1, splittedValues[i].toString().length() - 1);
                    }
                }
                valueString = valueStringloc;
                return false;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return false;
    }

    /**
     *
     * @return
     */
    public int getYearCondition() {
        return yearCondition;
    }

    /**
     *
     * @return
     */
    public Float[] getValueFloat() {
        return valueFloat;
    }

    /**
     *
     * @return
     */
    public String[] getValueString() {
        return valueString;
    }
}
