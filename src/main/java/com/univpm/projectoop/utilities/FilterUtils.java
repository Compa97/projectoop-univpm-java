package com.univpm.projectoop.utilities;

import com.univpm.projectoop.model.Delivery;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.regex.Pattern;

import static java.lang.Float.parseFloat;

public class FilterUtils<T> {

    private int flag, yearCondition;
    private Object [] splittedValues;
    private Float [] valueFloat;
    private String [] valueString;

    public static boolean check(Object value, String operator, Object th) {
        if (th instanceof Number && value instanceof Number) {
            Double thC = ((Number) th).doubleValue();
            Double valuec = ((Number) value).doubleValue();
            if (operator.equals("=="))
                return value.equals(th);
            else if (operator.equals(">"))
                return valuec > thC;
            else if (operator.equals("<"))
                return valuec < thC;
        } else if (th instanceof String && value instanceof String)
            return value.equals(th);
        return false;
    }


    public Collection<Delivery> select(Collection<Delivery> src, String fieldName, String operator, Object... value) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Collection<Delivery> out = new ArrayList<>();
        Method m;
        Object tmp;

        for (Delivery item : src) {

            if (isYear(fieldName)) {
                tmp = item.getYearPerc(yearCondition);
            } else {
                m = item.getClass().getMethod("get" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1), null);
                tmp = m.invoke(item);
            }

            String strValue = value[0].toString();
            isArray(strValue);

            switch (flag) {
                case 1:
                    if (FilterUtils.check(tmp, operator, valueFloat))
                        out.add(item);
                    break;

                case 2:
                    if (FilterUtils.check(tmp, operator, valueString))
                        out.add(item);
                    break;

                case 0:

                    if (Pattern.matches("([0-9]*[.])?[0-9]+", value[0].toString())) {
                        try {
                            Float number = parseFloat(value[0].toString());
                            if (FilterUtils.check(tmp, operator, number))
                                out.add(item);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else {
                        if (Pattern.matches("[\"]?[a-zA-Z]+[0-9]*[\"]?", value[0].toString())) {
                            String text = String.valueOf(value[0]);
                            if (FilterUtils.check(tmp, operator, text))
                                out.add(item);
                        }
                    }
                    break;

                default:
                    System.out.println("Errore");
                    break;
            }
        }
        return out;
    }

    private boolean isYear (String fieldName){

        boolean isYear;
        try{
            yearCondition = Integer.parseInt(fieldName);
            isYear = true;
        } catch (Exception e){
            //e.printStackTrace();
            isYear = false;
        }
        return isYear;
    }

    private void isArray (String strValue){

        if (strValue.contains("[")){
            String copy = strValue.substring(1, strValue.length()-1);
            splittedValues = copy.split(",");

            if(Pattern.matches("([0-9]+[.])?[0-9]+", splittedValues[0].toString())) {

                Float [] valueFloatloc = new Float[splittedValues.length];
                try {
                    for (int i = 0; i < splittedValues.length; i++) {
                        valueFloatloc[i] = parseFloat(splittedValues[i].toString());
                    }
                    valueFloat = valueFloatloc;
                    flag = 1;
                } catch (Exception e) {
                    e.printStackTrace();
                }

            } else if (Pattern.matches("[\"]?[a-zA-Z]+[0-9]*[\"]?", splittedValues[0].toString())) {

                String [] valueStringloc = new String[splittedValues.length];
                try {
                    for (int i = 0; i < splittedValues.length; i++) {
                        valueStringloc[i] = splittedValues[i].toString().substring(1,splittedValues[i].toString().length()-1);
                    }
                    valueString = valueStringloc;
                    flag = 2;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } else flag = 0;
    }
}