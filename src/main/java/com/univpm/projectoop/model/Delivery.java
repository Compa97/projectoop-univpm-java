package com.univpm.projectoop.model;

import com.fasterxml.jackson.annotation.JsonPropertyDescription;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.univpm.projectoop.utilities.CSVParser.COMMA_DELIMITER;

public class Delivery {
    //FREQ; UNIT; INDIC_PS; GEO\TIME_PERIOD,2012 ,2013 ,2014 ,2015 ,2016 ,2017

    @JsonPropertyDescription("Frequenza campione (A = Annuale)")
    private String freq;
    @JsonPropertyDescription("Inserire descrizione")
    private String unit;
    @JsonPropertyDescription("Codice tipo di spedizione (QOS801=...., QOS801 = ....., QOS801 = ....)")
    private String indic_PS;
    @JsonPropertyDescription("Nazione considerata")
    private String geo;
    @JsonPropertyDescription("Percentuale lettere consegnate ogni anno")
    private Float[] perTime_Period;   //usare una hashmap?

    public Delivery(String freq, String unit, String indic_PS, String geo, String TimePeriod) {
        this.freq = freq;
        this.unit = unit;
        this.indic_PS = indic_PS;
        this.geo = geo;
        String[] period = new String [4];
        period = TimePeriod.split(COMMA_DELIMITER);
        Pattern number = Pattern.compile("\\d+\\.\\d+\\s");
        Matcher m;

        perTime_Period = new Float [6];
        int n = period.length;
        String control_unv = "";
        for (int i = 1; i < n; i++) {
            if(!(period[i].charAt(0) == ':')) {
                m = number.matcher(period[i]);
                if (m.matches()) {
                    this.perTime_Period[i - 1] = Float.parseFloat(period[i]);
                }
                else {
                    this.perTime_Period[i - 1] = Float.parseFloat(period[i].split(" ")[0]);
                }
            }
            else switch (control_unv) {
                case ": ":
                    //not available
                    this.perTime_Period[i - 1] = null;
                    break;
                case ": b":
                    //break in time series
                    this.perTime_Period[i - 1] = null;
                    break;
                case ": c":
                    //confidential
                    this.perTime_Period[i - 1] = null;
                    break;
                case ": d":
                    //definition differs
                    this.perTime_Period[i - 1] = null;
                    break;
                case ": z":
                    //not applicable
                    this.perTime_Period[i - 1] = null;
                    break;
                default:
                    this.perTime_Period[i - 1] = null;
                    break;
            }
        }
    }

    public Delivery(ArrayList<Delivery> list) {
    }

    @Override
    public String toString() {

        String out = "Delivery { \n" +
                "freq='" + freq + '\'' + ", \n" +
                "unit='" + unit + '\'' + ", \n" +
                "indic_PS='" + indic_PS + '\'' + ", \n" +
                "geo='" + geo + '\'' + ", \n" +
                "perTime_Period=" + "[";
        int year = 2012;
        StringBuilder periods =new StringBuilder("") ;

        for (int i = 0; i < 6; i++){
            periods.append(year+i + ": " + perTime_Period[i] + ", ");
        }

        return out + periods + ']' + "\n" + '}';
    }



    public Float getYearPerc(int year){
        year-=2012;
        return this.perTime_Period[year];
    }

    //non necessario
   /* public float avg (){

        float total = 0;
        for (int i = 0; i < this.perTime_Period.length; i++){

            total += this.perTime_Period[i];
        }
        return total/this.perTime_Period.length;
    }

    public float max (float[] perTime_Period){

        Arrays.sort(perTime_Period);
        return perTime_Period[perTime_Period.length-1];
    }

    public float min (float[] perTime_Period){

        Arrays.sort(perTime_Period);
        float min = 0;
        int i = 0;
        while (!(min > 0) && i < perTime_Period.length){
            min = perTime_Period[i];
            i++;
        }

        return min;
    }
*/
    public String getFreq() {
        return freq;
    }

    public void setFreq(String freq) {
        this.freq = freq;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public String getIndic_PS() {
        return indic_PS;
    }

    public void setIndic_PS(String indic_PS) {
        this.indic_PS = indic_PS;
    }

    public String getGeo() {
        return geo;
    }

    public void setGeo(String geo) {
        this.geo = geo;
    }

    public Float[] getPerTime_Period() {
        return perTime_Period;
    }

    public void setPerTime_Period(Float[] perTime_Period) {
        this.perTime_Period = perTime_Period;
    }
}
