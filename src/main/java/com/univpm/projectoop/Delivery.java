package com.univpm.projectoop;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.univpm.projectoop.CSVParser.COMMA_DELIMITER;

public class Delivery {
    //FREQ; UNIT; INDIC_PS; GEO\TIME_PERIOD,2012 ,2013 ,2014 ,2015 ,2016 ,2017
    private String freq;
    private String unit;
    private String indic_PS;
    private String geo;
    private float[] perTime_Period;

    @Override
    public String toString() {

        String out = "Delivery { \n" +
                "freq='" + freq + '\'' + ", \n" +
                "unit='" + unit + '\'' + ", \n" +
                "indic_PS='" + indic_PS + '\'' + ", \n" +
                "geo='" + geo + '\'' + ", \n" +
                "perTime_Period=" + "[";
        int year = 2012;
        String periods = "";

        for (int i = 0; i < 6; i++){
            periods += year+i + ": " + perTime_Period[i] + ", ";
        }

        return out + periods + ']' + "\n" + '}';
    }

    public Delivery(String freq, String unit, String indic_PS, String geo, String TimePeriod) {
        this.freq = freq;
        this.unit = unit;
        this.indic_PS = indic_PS;
        this.geo = geo;
        String[] period = new String [4];
        period = TimePeriod.split(COMMA_DELIMITER);
        Pattern number = Pattern.compile("\\d+\\.\\d+\\s");
        Matcher m;

        perTime_Period = new float [6];
        int n = period.length;
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
            else this.perTime_Period[i - 1] = 0 ;
        }
    }

}

