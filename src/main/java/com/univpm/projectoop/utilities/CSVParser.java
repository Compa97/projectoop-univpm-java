package com.univpm.projectoop.utilities;

import com.univpm.projectoop.model.Delivery;

import java.io.*;
import java.util.*;

/**
 *
 */
public class CSVParser {

    /**
     *
     */
    public final static String SEMICOLON_DELIMITER = ";";

    /**
     *
     */
    public final static String COMMA_DELIMITER = ",";

    /**
     *
     */
    private static final ArrayList<Delivery> list= new ArrayList<>();

    /*
    public CSVParser(){
    }
    */

    /**
     *
     * @return
     */
    public static ArrayList<Delivery> getList() {
        return list;
    }

    /**
     *
     */
    public void parse() {

        try (BufferedReader br = new BufferedReader(new FileReader("to_parse.csv"))) {
            String line = br.readLine();
            while ((line = br.readLine()) != null) {
                String[] values = line.split(SEMICOLON_DELIMITER);
                list.add(new Delivery(values[0], values[1],values[2],values[3].split(COMMA_DELIMITER)[0],values[3]));
            }
        }catch (FileNotFoundException e){
            System.out.println("File not found");
            e.printStackTrace();
        } catch (IOException i) {
            i.printStackTrace();
        }
    }

}

