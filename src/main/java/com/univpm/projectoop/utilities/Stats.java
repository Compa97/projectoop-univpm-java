package com.univpm.projectoop.utilities;

import com.univpm.projectoop.model.Deliveries;

import static com.univpm.projectoop.Main.deliveries;

public class Stats{

    /**
     * Media aritmetica
     */
    private float avg;

    /**
     * Valore numerico massimo
     */
    private float max;

    /**
     * Valore numerico minimo
     */
    private float min;

    /**
     * Deviazione standard
     */
    private float devStd;

    /**
     * Conteggio delle istanze
     */
    private int count;

    /**
     * Costruttore riferito alle statistiche sull'intero dataset in base all'anno
     * @param year
     */
    public Stats(int year){
        this.avg = deliveries.getMeanOfYear(year);
        this.max = deliveries.maxOfaYear(year);
        this.min = deliveries.minOfaYear(year);
        this.devStd = deliveries.devStd(year);
        this.count = deliveries.getDeliveriesList().size();
    }

    /**
     * Costruttore riferito alle statistiche su un insieme di oggetti filtrato
     * per altri parametri aggiuntivi oltre all'anno
     * @param year
     * @param list
     */
    public Stats(int year, Deliveries list){
        this.avg = list.getMeanOfYear(year);
        this.max = list.maxOfaYear(year);
        this.min = list.minOfaYear(year);
        this.devStd = list.devStd(year);
        this.count = list.getDeliveriesList().size();
    }

    /*
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

    */
}