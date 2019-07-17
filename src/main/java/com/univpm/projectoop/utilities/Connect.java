package com.univpm.projectoop.utilities;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.io.BufferedReader;

public class
Connect {
    private final String url = "http://data.europa.eu/euodp/data/api/3/action/package_show?id=TXJLP91qYJyBYbBF4uug";
    private String data;

    public String getData() {

        return data;
    }

    public String getUrl() {

        return url;
    }

    public Connect() {

        data = "";
    }

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
            System.out.println("Unable to parse the URL correctly ");
        } catch (IOException e) {
            System.out.println("IOexception");
        }
    }
}




