package com.univpm.projectoop.utilities;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

public class FilterUtils<T> {
    public static boolean check(Object value, String operator, Object... obj) {

        if (value != null) {

            if (obj.length == 1 && obj[0] instanceof Number && value instanceof Number) {

                Float objF = ((Number) obj[0]).floatValue();
                Float valueF = ((Number) value).floatValue();

                if (operator.equals("$eq")) {
                    Float prova = Float.valueOf(String.valueOf(valueF));
                    Float prova1 = Float.valueOf(String.valueOf(objF));
                    boolean b = prova.equals(prova1);
                    return b;
                } else if (operator.equals("$not"))
                    return !value.equals(obj[0]);
                else if (operator.equals("$gt"))
                    return valueF > objF;
                else if (operator.equals("$gte"))
                    return valueF >= objF;
                else if (operator.equals("$lt"))
                    return valueF < objF;
                else if (operator.equals("$lte"))
                    return valueF <= objF;

            } else if (obj.length == 1 && obj[0] instanceof String && value instanceof String) {

                if (operator.equals("$eq") || operator.equals("$in"))
                    return ((String) value).equals((String) obj[0]);
                else if (operator.equals("$not") || operator.equals("$nin")) return !value.equals(obj[0]);

            } else if (obj.length > 1) {

                if (operator.equals("$bt")) {
                    if (obj.length == 2 && obj[0] instanceof Number && obj[1] instanceof Number) {
                        Float min = ((Number) obj[0]).floatValue();
                        Float max = ((Number) obj[1]).floatValue();
                        Float valueF = ((Number) value).floatValue();
                        return valueF >= min && valueF <= max;
                    }
                } else if (operator.equals("$in")){
                    boolean b = Arrays.asList(obj).contains(value);
                    return b;
                }

                else if (operator.equals("$nin")) {
                    boolean b = !Arrays.asList(obj).contains(value);
                    return b;
                }
            }
        }

        return false;
    }


    public Collection<T> select(Collection<T> src, String fieldName, String operator, Object value) {
        Collection<T> out = new ArrayList<T>();
        for (T item : src) {
            try {
                Method m = item.getClass().getMethod("get" + fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1), null);
                try {
                    Object tmp = m.invoke(item);
                    if (FilterUtils.check(tmp, operator, value))
                        out.add(item);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (IllegalArgumentException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                }
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            } catch (SecurityException e) {
                e.printStackTrace();
            }
        }
        return out;
    }

}