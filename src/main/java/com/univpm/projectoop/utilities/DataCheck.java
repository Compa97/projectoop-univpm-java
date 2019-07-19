package com.univpm.projectoop.utilities;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.regex.Pattern;

import static java.lang.Float.parseFloat;

/**
 * Classe che implementa metodi ausiliari per il filtraggio
 */
public class DataCheck {

    /**
     * Contiene l'anno se è utilizzato come campo
     */
    private int yearCondition;

    /**
     * Vettore con i valori da confrontare se numerici
     */
    private Float [] valueFloat;

    /**
     * Vettore con i valori da confrontare se stringhe
     */
    private String [] valueString;

    /**
     * Metodo che restituisce l'eventuale presenza di un anno come campo (vero/falso)
     * @param fieldName Nome del campo
     * @return true: il campo è un anno, false: il campo non è un anno
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
     * Controlla se i valori da confrontare sono inseriti in un vettore e di che tipo sono
     * @param strValue Valore di confronto nel filtro
     * @return true: dati numerici (vettoriali o singoli), false: dati stringhe (vettoriali o singoli)
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
     * Metodo che restituisce l'anno per il campo (se presente)
     * @return Anno come intero
     */
    public int getYearCondition() {
        return yearCondition;
    }

    /**
     * Restituisce il vettore di valori da confrontare se numerici
     * @return Vettore di float
     */
    public Float[] getValueFloat() {
        return valueFloat;
    }

    /**
     * Restituisce il vettore di valori da confrontare se stringhe
     * @return Vettore di stringhe
     */
    public String[] getValueString() {
        return valueString;
    }
}
