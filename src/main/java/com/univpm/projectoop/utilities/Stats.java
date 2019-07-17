package com.univpm.projectoop.utilities;

import com.univpm.projectoop.model.Deliveries;

import static com.univpm.projectoop.Main.deliveries;

public class Stats{
    private float avg;
    private float max;
    private float min;
    private float devStd;
    private int count;

    //statistiche sull'intero dataset
    public Stats(int year){
        avg = deliveries.getMeanOfYear(year);
        max = deliveries.maxOfaYear(year);
        min = deliveries.minOfaYear(year);
        devStd = deliveries.devStd(year);
        count = deliveries.count(deliveries.getDeliveriesList());
    }
    //statistiche su un insieme di oggetti
    public Stats(int year, Deliveries list){
        avg = list.getMeanOfYear(year);
        max = list.maxOfaYear(year);
        min = list.minOfaYear(year);
        devStd = list.devStd(year);
        count = list.count(list.getDeliveriesList());
    }

    public float getAvg() {
        return avg;
    }

    public void setAvg(float avg) {
        this.avg = avg;
    }

    public float getMax() {
        return max;
    }

    public void setMax(float max) {
        this.max = max;
    }

    public float getMin() {
        return min;
    }

    public void setMin(float min) {
        this.min = min;
    }

    public float getDevStd() {
        return devStd;
    }

    public void setDevStd(float devStd) {
        this.devStd = devStd;
    }
}