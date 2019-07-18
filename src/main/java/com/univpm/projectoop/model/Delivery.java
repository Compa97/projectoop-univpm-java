package com.univpm.projectoop.model;

import com.fasterxml.jackson.annotation.JsonPropertyDescription;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.univpm.projectoop.utilities.CSVParser.COMMA_DELIMITER;

/**
 * DataSet Eurostat: percentage of letters delivered on-time
 */
public class Delivery {
    //FREQ; UNIT; INDIC_PS; GEO\TIME_PERIOD,2012 ,2013 ,2014 ,2015 ,2016 ,2017

    /**
     *
     */
    @JsonPropertyDescription("Frequenza campione (A = Annuale)")
    private String freq;

    /**
     *
     */
    @JsonPropertyDescription("Inserire descrizione")
    private String unit;

    /**
     * Codici tipo di spedizione:
     * -QOS801: indica una spedizione nazionale in 1 giorno lavorativo (dal deposito alla consegna)
     * -QOS803: indica una spedizione internazionale in (massimo) 3 giorni lavorativi (dal deposito alla consegna)
     * -QOS804: indica una spedizione internazionale in (massimo) 5 giorni lavorativi (dal deposito alla consegna)
     */
    @JsonPropertyDescription("Codice tipo di spedizione (QOS801= nazionale - 1 giorno; QOS803 = internazionale - 3 giorni; QOS801 = internazionale - 5 giorni)")
    private String indic_PS;

    /**
     *
     */
    @JsonPropertyDescription("Nazione considerata")
    private String geo;

    /**
     *
     */
    @JsonPropertyDescription("Percentuale lettere consegnate ogni anno")
    private Float[] perTime_Period;

    /**
     *
     * @param freq
     * @param unit
     * @param indic_PS
     * @param geo
     * @param TimePeriod
     */
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
                    //if there is some text appending to numeric data
                    this.perTime_Period[i - 1] = Float.parseFloat(period[i].split(" ")[0]);
                }
            }
            else if (control_unv.contains(":")) {
                //if is: B (break in time series), C (confidential), D (definition differs), Z (not applicable)
                this.perTime_Period[i - 1] = null;
            }
        }
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
        StringBuilder periods = new StringBuilder() ;

        for (int i = 0; i < 6; i++){
            periods.append(year).append(i).append(": ").append(perTime_Period[i]).append(", ");
        }

        return out + periods + ']' + "\n" + '}';
    }

    public Float getYearPerc(int year){
        year -= 2012;
        return this.perTime_Period[year];
    }

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