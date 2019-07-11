package com.univpm.projectoop.model;

import com.univpm.projectoop.utilities.Filter;
import com.univpm.projectoop.utilities.FilterUtils;

import javax.validation.constraints.Null;
import java.util.ArrayList;
import java.util.Iterator;

public class Deliveries implements Filter<Delivery, Object> {
    private  ArrayList<Delivery> deliveriesList;
    private FilterUtils<Delivery> utils;

    public Deliveries(ArrayList<Delivery> deliveriesList, FilterUtils<Delivery> utils) {
        super();
        this.deliveriesList = deliveriesList;
        this.utils = utils;
    }

    public Deliveries(ArrayList<Delivery> deliveriesList) {
        super();
        this.deliveriesList = deliveriesList;
        this.utils = new FilterUtils<Delivery>();
    }

    public ArrayList<Delivery> getDeliveriesList() {
        return deliveriesList;
    }

    public void setDeliveriesList(ArrayList<Delivery> deliveriesList) {

        this.deliveriesList = deliveriesList;
    }

    public float getMeanOfYear(int year) {
        float mean = 0;
        int counter = 0;
        for (Delivery d : deliveriesList) {
            if ((d.getYearPerc(year) != null)){
                mean+=d.getYearPerc(year);
            }
            else{
                counter++;
            }
        }
        return (mean / (deliveriesList.size()-counter));
    }

    public float devStd (int year) {
        int counter = 0;
       float mean = getMeanOfYear(year);
       float qsum = 0;
        for (Delivery d : deliveriesList){
            if (d.getYearPerc(year) != null){
                qsum += (d.getYearPerc(year)-mean)*(d.getYearPerc(year)-mean);
            }
            else{
                counter++;
            }
        }

        return (float) Math.sqrt(qsum/(deliveriesList.size()-counter));

    }

    public float maxOfaYear( int year){
        int i =0;
        while (deliveriesList.get(i).getYearPerc(year)==null){
            i++;
        }
        float max =deliveriesList.get(i).getYearPerc(year);
        for (Delivery d : deliveriesList) {
            if(d.getYearPerc(year)!=null){
                if (max < d.getYearPerc(year)) {
                    max=d.getYearPerc(year);
                }
            }
        }
        return max;
    }

    public float minOfaYear(int year){
        int i =0;
        while (deliveriesList.get(i).getYearPerc(year)==null){
            i++;
        }
        float min =deliveriesList.get(i).getYearPerc(year);
        for (Delivery d : deliveriesList) {
            if(d.getYearPerc(year)!=null){
                if (min > d.getYearPerc(year)) {
                    min=d.getYearPerc(year);
                }
            }
        }
        return min;
    }

    public int count (String att, String value) {
        int count = 0;
        switch (att) {
            case "geo" :
                for (Delivery d : deliveriesList) {
                    if(d.getGeo()==value)count+=1;
                }
                break;
            case "code":
                for (Delivery d : deliveriesList) {
                    if(d.getIndic_PS() ==value)count+=1;
                }
                break;
        }
        return count;
    }

    @Override
    public ArrayList<Delivery> filterField(String fieldName, String operator, Object value) {

        return (ArrayList<Delivery>) utils.select(this.getDeliveriesList(), fieldName, operator, value);
    }


}