package com.univpm.projectoop.model;

import com.fasterxml.jackson.annotation.JsonPropertyDescription;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.univpm.projectoop.utilities.CSVParser.COMMA_DELIMITER;

/**
 * DataSet EUROSTAT: percentage of letters delivered on-time
 * Model class contenente le singole istanze: percentuale di lettere consegnate
 * per un determinato paese e tipo di spedizione negli anni considerati.
 */
public class Delivery {

    /**
     * Frequenza del rilevamento
     */
    @JsonPropertyDescription("Frequenza campione (A = Annuale)")
    private String freq;

    /**
     * Unità di misura (percentuale %)
     */
    @JsonPropertyDescription("Unità di misura (PC = PerCentuale")
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
     * Sigla di riferimento per il paese dell'Unione Europea considerato
     */
    @JsonPropertyDescription("Nazione considerata")
    private String geo;

    /**
     * Percentuale lettere consegnate suddivise per anno ([0-5] corrisponde a [2012-2017])
     */
    @JsonPropertyDescription("Percentuale lettere consegnate ogni anno")
    private Float[] perTime_Period;

    /**
     * Costruttore che inizializza l'oggetto ed effettua il parsing (a prescindere
     * dalla loro classificazione, i dati non disponibili e le eventuali annotazioni
     * su dati parziali non sono stati presi in considerazione).
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

    /**
     * Metodo toString
     * @return Descrizione oggetto
     */
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

    /**
     * Restituisce il valore percentuale corrispondente all'anno indicato
     * @param year
     * @return Istanza float del vettore perTime_Period corrispondente all'anno richiesto
     */
    public Float getYearPerc(int year){
        year -= 2012;
        return this.perTime_Period[year];
    }

    /*
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
    */

    /**
     * Restituisce il codice di riferimento al gruppo di tipologie di spedizioni considerate
     * @return Stringa che indica il tipo di spedizione ("QOS801", "QOS803", "QOS804")
     */
    public String getIndic_PS() {
        return indic_PS;
    }

    /**
     * Permette di modificare il codice spedizione per l'oggetto istanziato
     * @param indic_PS
     */
    public void setIndic_PS(String indic_PS) {
        this.indic_PS = indic_PS;
    }

    /**
     * Restituisce il paese corrispondente all'oggetto considerato
     * @return Stringa corrispondente al codice del paese UE considerato nell'oggetto istanziato
     */
    public String getGeo() {
        return geo;
    }

    /**
     * Permette di modificare il codice del paese dell'oggetto
     */
    public void setGeo(String geo) {
        this.geo = geo;
    }

    /**
     * Restituisce il vettore contenente i dati percentuali per tutti gli anni considerati
     * @return
     */
    public Float[] getPerTime_Period() {
        return perTime_Period;
    }

    /**
     * Permette di modificare l'intero vettore con i dati percentuali degli anni considerati
     * @param perTime_Period
     */
    public void setPerTime_Period(Float[] perTime_Period) {
        this.perTime_Period = perTime_Period;
    }
}