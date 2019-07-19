package com.univpm.projectoop.utilities;

import com.univpm.projectoop.model.Delivery;

import java.io.*;
import java.util.*;

/**
 * Classe che implementa il parsing del CSV
 */
public class CSVParser {

    /**
     * Stringa delimitatore: punto e virgola
     */
    public final static String SEMICOLON_DELIMITER = ";";

    /**
     * Stringa delimitatore: virgola
     */
    public final static String COMMA_DELIMITER = ",";

    /**
     * Lista contenente gli oggetti corrispondenti alle righe del CSV
     */
    private static final ArrayList<Delivery> list = new ArrayList<>();

    /**
     * Costruttore della classe
     */
    public CSVParser(){
    }

    /**
     * Metodo che restituisce la lista di oggetti
     * @return Lista di oggetti corrispondenti al contenuto del CSV
     */
    public static ArrayList<Delivery> getList() {
        return list;
    }

    /**
     * Metodo che effettua il parsing del CSV
     * @throws FileNotFoundException Lanciato se il file da leggere non viene trovato nella cartella
     * @throws IOException Lanciato in caso di problemi di I/O
     */
    public void parse() throws FileNotFoundException, IOException {

        try (BufferedReader br = new BufferedReader(new FileReader("to_parse.csv"))) {
            String line = br.readLine();
            while ((line = br.readLine()) != null) {
                String[] values = line.split(SEMICOLON_DELIMITER);
                list.add(new Delivery(values[0], values[1],values[2],values[3].split(COMMA_DELIMITER)[0],values[3]));
            }
        }
    }

}

