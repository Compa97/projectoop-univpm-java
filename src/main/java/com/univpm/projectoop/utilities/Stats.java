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
        this.avg = deliveries.getMeanOfYear(year);
        this.max = deliveries.maxOfaYear(year);
        this.min = deliveries.minOfaYear(year);
        this.devStd = deliveries.devStd(year);
        this.count = deliveries.getDeliveriesList().size();
    }
    //statistiche su un insieme di oggetti
    public Stats(int year, Deliveries list){
        this.avg = list.getMeanOfYear(year);
        this.max = list.maxOfaYear(year);
        this.min = list.minOfaYear(year);
        this.devStd = list.devStd(year);
        this.count = list.getDeliveriesList().size();
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

    public float getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}