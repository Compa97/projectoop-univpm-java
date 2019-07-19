package com.univpm.projectoop.utilities;

import com.univpm.projectoop.model.Deliveries;
import static com.univpm.projectoop.Main.deliveries;

/**
 *
 */
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
     * @param year Anno delle statistiche
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
     * @param year Anno delle statistiche
     * @param list Lista oggetti filtrata
     */
    public Stats(int year, Deliveries list) {
        this.avg = list.getMeanOfYear(year);
        this.max = list.maxOfaYear(year);
        this.min = list.minOfaYear(year);
        this.devStd = list.devStd(year);
        this.count = list.getDeliveriesList().size();
    }

    /**
     * Metodo per ottenere la media nella statistica considerata
     * @return Media
     */
    public float getAvg() {
        return avg;
    }

    /**
     * Metodo per modificare la media nella statistica considerata
     * @param avg Media
     */
    public void setAvg(float avg) {
        this.avg = avg;
    }

    /**
     * Metodo per ottenere il massimo nella statistica considerata
     * @return Massimo
     */
    public float getMax() {
        return max;
    }

    /**
     * Metodo per modificare il massimo nella statistica considerata
     * @param max Massimo
     */
    public void setMax(float max) {
        this.max = max;
    }

    /**
     * Metodo per ottenere il minimo nella statistica considerata
     * @return Minimo
     */
    public float getMin() {
        return min;
    }

    /**
     * Metodo per modificare il minimo nella statistica considerata
     * @param min Minimo
     */
    public void setMin(float min) {
        this.min = min;
    }

    /**
     * Metodo per ottenere la deviazione standard nella statistica considerata
     * @return Deviazione standard
     */
    public float getDevStd() {
        return devStd;
    }

    /**
     * Metodo per modificare la deviazione standard nella statistica considerata
     * @param devStd Deviazione standard
     */
    public void setDevStd(float devStd) {
        this.devStd = devStd;
    }

    /**
     * Metodo per ottenere il numero di istanze nella statistica considerata
     * @return Conto istanze
     */
    public float getCount() {
        return count;
    }

    /**
     * Metodo per modificare il numero di istanze nella statistica considerata
     * @param count Conto istanze
     */
    public void setCount(int count) {
        this.count = count;
    }
}