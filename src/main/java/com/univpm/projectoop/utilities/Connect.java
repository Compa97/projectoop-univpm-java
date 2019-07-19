package com.univpm.projectoop.utilities;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.io.BufferedReader;

/**
 * Classe che effettua il download del JSON
 */
public class Connect {

    /**
     * Stringa contenente l'URL che restituisce il JSON del dataset di progetto
     */
    private final static String url = "http://data.europa.eu/euodp/data/api/3/action/package_show?id=TXJLP91qYJyBYbBF4uug";

    /**
     * Contenuto del JSON
     */
    private String data;

    /**
     * Metodo che restituisce il contenuto del JSON
     * @return JSON come Stringa
     */
    public String getData() {
        return data;
    }

    /**
     * Metodo che restituisce l'URL
     * @return Stringa URL
     */
    public String getUrl() {
        return url;
    }

    /**
     * Costruttore della classe che inizialliza la stringa in cui inserire il JSON
     */
    public Connect() {
        data = "";
    }

    /**
     * Metodo che effettua la connessione e la lettura del JSON
     */
    public void startConnect() {

        try {
            URLConnection openConnection = new URL(url).openConnection();
            openConnection.addRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64; rv:25.0) Gecko/20100101 Firefox/25.0");
            InputStreamReader inR;
            try (InputStream in = openConnection.getInputStream()) {
                inR = new InputStreamReader(in);
                BufferedReader buf = new BufferedReader(inR);
                String line = "";
                while ((line = buf.readLine()) != null) {
                    data = data.concat(line);
                }
            }

        } catch (MalformedURLException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Unable to parse the URL correctly.");

        } catch (IOException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "I/O Exception.");
        }
    }
}




