package com.univpm.projectoop.model;

import com.univpm.projectoop.utilities.Filter;
import com.univpm.projectoop.utilities.FilterUtils;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

/**
 * Collection model: classe che definisce l'oggetto lista di Delivery
 */
public class Deliveries implements Filter<Delivery, Object> {

    /**
     * Lista degli oggetti Delivery
     */
    private ArrayList<Delivery> deliveriesList;

    /**
     * Lista utilizzata per filtrare gli oggetti
     */
    private final FilterUtils<Delivery> utils;

    /**
     * Costruttore che assegna la lista di tutti gli oggetti e quella da filtrare
     * @param deliveriesList
     */
    public Deliveries(ArrayList<Delivery> deliveriesList) {
        super();
        this.deliveriesList = deliveriesList;
        this.utils = new FilterUtils<>();
    }

    /**
     * Metodo che ritorna la lista di tutti gli oggetti
     * @return
     */
    public ArrayList<Delivery> getDeliveriesList() {
        return deliveriesList;
    }

    /**
     * Metodo che permette di modificare la lista di tutti gli oggetti
     * @param deliveriesList
     */
    public void setDeliveriesList(ArrayList<Delivery> deliveriesList) {
        this.deliveriesList = deliveriesList;
    }

    /**
     * Metodo per il calcolo della media della percentuale in un determinato anno
     * @param year
     * @return La media con virgola della percentuale di lettere consegnate nell'anno considerato
     */
    public float getMeanOfYear(int year) {
        float mean = 0;
        int counter = 0;
        for (Delivery d : deliveriesList) {
            if ((d.getYearPerc(year) != null)){
                mean += d.getYearPerc(year);
            }
            else{
                counter++;
            }
        }
        return (mean / (deliveriesList.size() - counter));
    }

    /**
     *
     * @param year
     * @return
     */
    public float devStd (int year) {
        int counter = 0;
        float mean = getMeanOfYear(year);
        float qsum = 0;
        for (Delivery d : deliveriesList){
            if (d.getYearPerc(year) != null){
                qsum += (d.getYearPerc(year) - mean) * (d.getYearPerc(year) - mean);
            }
            else{
                counter++;
            }
        }

        return (float) Math.sqrt(qsum/(deliveriesList.size() - counter));
    }

    /**
     *
     * @param year
     * @return
     */
    public float maxOfaYear(int year){
        int i = 0;
        while (deliveriesList.get(i).getYearPerc(year) == null){
            i++;
        }
        float max = deliveriesList.get(i).getYearPerc(year);
        for (Delivery d : deliveriesList) {
            if(d.getYearPerc(year) != null){
                if (max < d.getYearPerc(year)) {
                    max = d.getYearPerc(year);
                }
            }
        }
        return max;
    }

    /**
     *
     * @param year
     * @return
     */
    public float minOfaYear(int year){
        int i = 0;
        while (deliveriesList.get(i).getYearPerc(year) == null){
            i++;
        }
        float min = deliveriesList.get(i).getYearPerc(year);
        for (Delivery d : deliveriesList) {
            if(d.getYearPerc(year) != null){
                if (min > d.getYearPerc(year)) {
                    min = d.getYearPerc(year);
                }
            }
        }
        return min;
    }

    /**
     *
     * @param objs
     * @return
     */
    public ArrayList<Delivery> and (ArrayList<ArrayList<Delivery>> objs) {
        ArrayList<Delivery> list = new ArrayList<>();
        for(int i = 0; i < objs.size(); i++) {
            for(Delivery d : objs.get(i)){
                boolean in = true;
                for(ArrayList<Delivery> ToCompare : objs) {
                    if(!ToCompare.contains(d)) {
                        in = false;
                        break;
                    }
                }
                if(in && !list.contains(d))
                    list.add(d);
            }
        }
        return list;
    }

    /**
     *
     * @param objs
     * @return
     */
    public ArrayList<Delivery> or (ArrayList<ArrayList<Delivery>> objs) {
        Set<Delivery> s = new HashSet<>();
        for (ArrayList<Delivery> elem : objs)
            s.addAll(elem);
        return new ArrayList<>(s);
    }

    /**
     *
     * @param fieldName
     * @param operator
     * @param value
     * @return Ritorna la lista generata dal metodo select della classe FilterUtils
     * @throws NoSuchMethodException Lanciato quando un metodo non viene trovato
     * @throws IllegalAccessException Lanciato quando un'applicazione non ha accesso alla
     * definizione di una classe, un campo, un metodo o un costruttore
     * @throws InvocationTargetException
     */
    @Override
    public ArrayList<Delivery> filterField(String fieldName, String operator, Object... value) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        return (ArrayList<Delivery>) utils.select(this.getDeliveriesList(), fieldName, operator, value);
    }
}