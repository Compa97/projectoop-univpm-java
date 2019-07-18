package com.univpm.projectoop.utilities;

import com.univpm.projectoop.model.Delivery;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

public class FilterUtils<T> {

    private static boolean check(Object value, String operator, Object... obj) {

        if (value != null) {

            if (obj.length == 1 && obj[0] instanceof Number && value instanceof Number) {

                Float objF = ((Number) obj[0]).floatValue();
                Float valueF = ((Number) value).floatValue();

                switch (operator) {
                    case "$eq":
                    case "$in":
                        Float prova = Float.valueOf(String.valueOf(valueF));
                        Float prova1 = Float.valueOf(String.valueOf(objF));
                        return prova.equals(prova1);
                    case "$not":
                    case "$nin":
                        return !value.equals(obj[0]);
                    case "$gt":
                        return valueF > objF;
                    case "$gte":
                        return valueF >= objF;
                    case "$lt":
                        return valueF < objF;
                    case "$lte":
                        return valueF <= objF;
                }

            } else if (obj.length == 1 && obj[0] instanceof String && value instanceof String) {

                if (operator.equals("$eq") || operator.equals("$in"))
                    return value.equals(obj[0]);
                else if (operator.equals("$not") || operator.equals("$nin")) return !value.equals(obj[0]);

            } else if (obj.length > 1) {

                switch (operator) {
                    case "$bt":
                        if (obj.length == 2 && obj[0] instanceof Number && obj[1] instanceof Number) {

                            Float min = ((Number) obj[0]).floatValue();
                            Float max = ((Number) obj[1]).floatValue();
                            if (min > max){
                                Float tmp = max;
                                max = min;
                                min = tmp;
                            }
                            Float valueF = ((Number) value).floatValue();
                            return valueF >= min && valueF <= max;
                        }
                        break;
                    case "$in":
                        return Arrays.asList(obj).contains(value);
                    case "$nin":
                        return !Arrays.asList(obj).contains(value);
                }
            }
        }

        return false;
    }

    public Collection<Delivery> select(Collection<Delivery> src, String fieldName, String operator, Object... value) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        Collection<Delivery> out = new ArrayList<>();
        Method m;
        Object tmp;

        DataCheck dc = new DataCheck();
        String strValue = value[0].toString();
        boolean dataType = dc.isArray(strValue);
        boolean operatorYear = dc.isYear(fieldName);
        boolean checkResult = false;
        Float [] floatV = dc.getValueFloat();
        String [] stringV = dc.getValueString();

        for (Delivery item : src) {

            if (operatorYear) {
                tmp = item.getYearPerc(dc.getYearCondition());
            } else {
                m = item.getClass().getMethod("get" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1), null);
                tmp = m.invoke(item);
            }

            if (dataType) {
                checkResult = check(tmp, operator, floatV);
            } else {
                checkResult = check(tmp, operator, stringV);
            }

            if (checkResult) out.add(item);
        }
        return out;
    }
}